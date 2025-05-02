import React, { useEffect, useState } from 'react';

function App() {
  const [singers, setSingers] = useState([]);
  const [locations, setLocations] = useState([]);
  const [selectedSingerId, setSelectedSingerId] = useState(null);
  const [selectedSingerName, setSelectedSingerName] = useState('');
  const [hireDate, setHireDate] = useState('');
  const [locationIndex, setLocationIndex] = useState('');

  useEffect(() => {
    fetch('/singers')
      .then(res => res.json())
      .then(data => setSingers(data))
      .catch(err => console.error('Failed to fetch singers:', err));

    fetch('http://192.168.0.11:8080/locations')
      .then(res => res.json())
      .then(data => setLocations(data))
      .catch(err => console.error('Failed to fetch locations:', err));
  }, []);

  const hireSingerAt = () => {
    const locationId = parseInt(locationIndex) + 1;

    fetch(`/singers/${selectedSingerId}/hireAt`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ date: hireDate, location: locationId }) // send locationId as location
    })
      .then(res => {
        if (!res.ok) throw new Error('Failed to hire');
        alert('Singer hired!');
        setSelectedSingerId(null);
        setHireDate('');
        setLocationIndex('');
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
              setSelectedSingerId(index + 1); // assumes singer ID = index + 1
              setSelectedSingerName(name);
            }}>
              Hire
            </button>
          </li>
        ))}
      </ul>

      {selectedSingerId && (
        <div style={{ marginTop: 20 }}>
          <h2>Hire {selectedSingerName}</h2>
          <input type="date" value={hireDate} onChange={e => setHireDate(e.target.value)} />
          <select value={locationIndex} onChange={e => setLocationIndex(e.target.value)}>
            <option value="">Select location</option>
            {locations.map((loc, i) => (
              <option key={i} value={i}>{loc}</option>
            ))}
          </select>
          <div>
            <button onClick={hireSingerAt}>Confirm</button>
            <button onClick={() => setSelectedSingerId(null)}>Cancel</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
