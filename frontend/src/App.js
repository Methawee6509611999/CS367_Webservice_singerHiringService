import React, { useEffect, useState } from 'react';

function App() {
  const [singers, setSingers] = useState([]);
  const [selectedSingerId, setSelectedSingerId] = useState(null);
  const [selectedSingerName, setSelectedSingerName] = useState('');
  const [hireDate, setHireDate] = useState('');
  const [location, setLocation] = useState('');

  useEffect(() => {
    fetch('/singers')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch singers');
        return res.json();
      })
      .then(data => setSingers(data))
      .catch(err => console.error(err));
  }, []);

  const hireSinger = () => {
    fetch(`/singers/${selectedSingerId}/hire`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ date: hireDate, location })
    })
      .then(res => {
        if (!res.ok) throw new Error('Failed to hire');
        alert('Singer hired!');
        setSelectedSingerId(null);
        setHireDate('');
        setLocation('');
      })
      .catch(err => {
        alert('Hiring failed');
        console.error(err);
      });
  };

  return (
    <div style={{ padding: 20 }}>
      <h1>Singers</h1>
      <ul>
        {singers.map((name, index) => (
          <li key={index}>
            {name}{' '}
            <button onClick={() => {
              setSelectedSingerId(index + 1); // assumes IDs = 1-based
              setSelectedSingerName(name);
            }}>
              Hire
            </button>
          </li>
        ))}
      </ul>

      {selectedSingerId && (
        <div>
          <h2>Hire {selectedSingerName}</h2>
          <input type="date" value={hireDate} onChange={e => setHireDate(e.target.value)} />
          <input type="text" placeholder="Location" value={location} onChange={e => setLocation(e.target.value)} />
          <button onClick={hireSinger}>Confirm</button>
          <button onClick={() => setSelectedSingerId(null)}>Cancel</button>
        </div>
      )}
    </div>
  );
}

export default App;
