import React, { useEffect, useState } from 'react';

function LocationSearch() {
  const [searchTerm, setSearchTerm] = useState('');
  const [locations, setLocations] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!searchTerm.trim()) {
      setLocations([]);
      setError('');
      return;
    }

    const url = `/singers/locations?name=${encodeURIComponent(searchTerm)}`;
    fetch(url)
      .then(res => {
        if (!res.ok) {
          if (res.status === 400) {
            throw new Error('Search term is required');
          }
          throw new Error('Failed to fetch locations');
        }
        return res.json();
      })
      .then(data => {
        console.log('Location response:', data);
        setLocations(Array.isArray(data) ? data : []);
        setError('');
      })
      .catch(err => {
        console.error('Fetch error:', err);
        setLocations([]);
        setError(err.message || 'Failed to load locations. Please try again.');
      });
  }, [searchTerm]);

  return (
    <div className="container">
      <style>{`
        .container {
          max-width: 600px;
          margin: 20px auto;
          padding: 20px;
          font-family: Arial, sans-serif;
          background-color: #f9f9f9;
          border-radius: 8px;
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h2 {
          color: #333;
          text-align: center;
          margin-bottom: 20px;
        }
        .search-bar {
          width: 100%;
          padding: 10px;
          font-size: 16px;
          border: 1px solid #ccc;
          border-radius: 4px;
          box-sizing: border-box;
        }
        .search-bar:focus {
          border-color: #007bff;
          outline: none;
        }
        ul {
          list-style: none;
          padding: 0;
          margin-top: 20px;
        }
        li {
          padding: 10px;
          background-color: #fff;
          margin-bottom: 8px;
          border-radius: 4px;
          box-shadow: 0 1px 2px rgba(0,0,0,0.1);
        }
        .error {
          color: #dc3545;
          text-align: center;
          margin: 10px 0;
        }
        .no-results {
          text-align: center;
          color: #666;
        }
      `}</style>

      <h2>Search Locations</h2>
      <input
        className="search-bar"
        type="text"
        placeholder="Enter location name"
        value={searchTerm}
        onChange={e => setSearchTerm(e.target.value)}
      />
      {error && <div className="error">{error}</div>}
      <ul>
        {Array.isArray(locations) && locations.length > 0 ? (
          locations.map((loc, index) => (
            <li key={index}>{loc}</li>
          ))
        ) : (
          <li className="no-results">{searchTerm ? 'No locations found' : 'Type to search'}</li>
        )}
      </ul>
    </div>
  );
}

export default LocationSearch;