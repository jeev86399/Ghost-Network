import React, { useState } from 'react';
import axios from 'axios';

/**
 * PHASE 5.0 CORE STYLES & ANIMATIONS
 */
const styleSheet = document.createElement("style");
styleSheet.innerText = `
  @keyframes ghostScanPulse {
    0%, 100% { opacity: 1; filter: brightness(1); }
    50% { opacity: 0.7; filter: brightness(1.3); }
  }
  @keyframes scanLineMove {
    from { top: 0%; }
    to { top: 100%; }
  }
  .threat-row:hover {
    background-color: rgba(30, 41, 59, 0.5);
  }
  .unmask-btn:hover {
    background-color: #7c3aed !important;
    transform: scale(1.05);
  }
`;
document.head.appendChild(styleSheet);

export default function App() {
  const [results, setResults] = useState([]);
  const [scanning, setScanning] = useState(false);
  const [previewContent, setPreviewContent] = useState(null); // For Ghost-Terminal

  const styles = {
    container: { backgroundColor: '#020617', color: '#f8fafc', minHeight: '100vh', padding: '40px', fontFamily: 'monospace' },
    header: { borderBottom: '1px solid #1e293b', marginBottom: '30px', paddingBottom: '10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
    btnPrimary: { backgroundColor: '#2563eb', color: 'white', border: 'none', padding: '12px 24px', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold', transition: '0.3s' },
    uploadBox: { background: '#0f172a', padding: '30px', borderRadius: '8px', border: '2px dashed #3b82f6', textAlign: 'center', marginTop: '20px' },
    stats: { display: 'flex', gap: '20px', margin: '20px 0' },
    card: { background: '#0f172a', padding: '15px', borderRadius: '8px', border: '1px solid #1e293b', flex: 1 },
    table: { width: '100%', borderCollapse: 'collapse', background: '#0f172a', borderRadius: '8px', overflow: 'hidden' },
    th: { textAlign: 'left', padding: '12px', color: '#64748b', fontSize: '11px', borderBottom: '1px solid #1e293b', textTransform: 'uppercase' },
    td: { padding: '12px', borderBottom: '1px solid #1e293b', fontSize: '13px' },
    pulseAnimation: { animation: 'ghostScanPulse 1.5s infinite' }
  };

  // --- PHASE 5.0 INTERACTIVE GHOST TERMINAL ---

  const handleUnmask = async (fileName) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/forensics/preview/${fileName}`);
      setPreviewContent({ name: fileName, data: response.data });
    } catch (e) {
      alert("Extraction Failed: Evidence link severed.");
    }
  };

  const PulseConsole = ({ lastResult, scanning }) => (
    <div style={{
      backgroundColor: '#000',
      border: '1px solid #1e293b',
      padding: '15px',
      margin: '20px 0',
      fontFamily: 'monospace',
      height: '140px',
      overflow: 'hidden',
      borderRadius: '4px',
      position: 'relative'
    }}>
      {scanning && <div style={{ position: 'absolute', left: 0, width: '100%', height: '2px', background: 'rgba(59, 130, 246, 0.5)', animation: 'scanLineMove 2s linear infinite', boxShadow: '0 0 15px #3b82f6' }} />}
      
      <div style={{ color: '#3b82f6', fontWeight: 'bold' }}>[SINGULARITY_PULSE_v5.0]: INTEL_LINK_ACTIVE</div>
      
      {scanning ? (
        <div style={styles.pulseAnimation}>
          <div style={{ color: '#10b981', marginTop: '5px' }}>  INGESTING: {lastResult?.fileName || 'DATA_STREAM...'}</div>
          <div style={{ color: '#fbbf24' }}>  SPECTER_INTEL: LINKING_SIMILAR_SIGNATURES...</div>
          <div style={{ color: '#ef4444' }}>  THREAT_SCORE: {lastResult?.threatScore?.toFixed(2) || '0.00'}%</div>
          <div style={{ color: '#64748b', fontSize: '10px', marginTop: '5px' }}>
            BYTE_RELATIONSHIP: MATCH_FOUND_IN_SECTOR_{Math.floor(Math.random()*100)}
          </div>
        </div>
      ) : (
        <div style={{ color: '#475569', marginTop: '35px', textAlign: 'center', letterSpacing: '3px' }}>
          {previewContent ? `>> VIEWING_UNMASKED_DATA: ${previewContent.name}` : "-- SYSTEM_STANDBY: AWAITING_ENCRYPTED_STREAM --"}
        </div>
      )}
    </div>
  );

  // --- LOGIC FUNCTIONS ---

  const handleUpload = async (event) => {
    const selectedFiles = Array.from(event.target.files);
    if (selectedFiles.length === 0) return;
    const formData = new FormData();
    selectedFiles.forEach(file => formData.append('files', file));
    setScanning(true);
    try {
      const response = await axios.post('http://localhost:8080/api/forensics/upload-multiple', formData);
      setResults(prev => [...response.data, ...prev]);
    } catch (e) {
      alert("Critical: Spectre Link Failure.");
    }
    setScanning(false);
  };

  const downloadBundle = () => {
    if (results.length === 0) return alert("No evidence gathered.");
    // This calls the bundle logic for the CSV
    const headers = "Identifier,Class,Entropy,Probability,SHA-256 Hash\n";
    const rows = results.map(r => `${r.fileName},${r.detectedType},${r.entropy.toFixed(2)},${r.threatScore.toFixed(2)}%,${r.hash}`).join("\n");
    const blob = new Blob([headers + rows], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `FORENSIC_EVIDENCE_BUNDLE_${Date.now()}.csv`;
    a.click();
  };

  return (
    <div style={styles.container}>
      {/* Header Bar */}
      <div style={styles.header}>
        <h1 style={{color: '#3b82f6', margin: 0, letterSpacing: '2px'}}>GHOST-NETWORK <span style={{color: '#64748b', fontSize: '16px'}}>v5.0 SINGULARITY</span></h1>
        <div style={{fontSize: '11px', color: '#a855f7', display: 'flex', alignItems: 'center', gap: '5px'}}>
          <div style={{width: '8px', height: '8px', borderRadius: '50%', backgroundColor: '#a855f7', boxShadow: '0 0 5px #a855f7'}}></div>
          INTEL_LINK_ESTABLISHED
        </div>
      </div>

      {/* Primary Actions */}
      <div style={{ marginBottom: '20px', display: 'flex', gap: '10px' }}>
        <button style={styles.btnPrimary} onClick={async () => {
          setScanning(true);
          try {
            const res = await axios.get('http://localhost:8080/api/forensics/scan');
            setResults(res.data);
          } catch(e) { alert("Core Scan Error."); }
          setScanning(false);
        }}>INITIATE DEEP SCAN</button>
        
        <button style={{...styles.btnPrimary, backgroundColor: '#a855f7'}} onClick={downloadBundle}>
          EXPORT EVIDENCE BUNDLE
        </button>
      </div>

      {/* Ingestion Portal */}
      <div style={styles.uploadBox}>
        <div style={{ display: 'flex', justifyContent: 'center', gap: '15px' }}>
          <label style={{...styles.btnPrimary, backgroundColor: '#1e293b', border: '1px solid #3b82f6'}}>📄 INGEST FILES
            <input type="file" multiple onChange={handleUpload} style={{display: 'none'}} />
          </label>
          <label style={{...styles.btnPrimary, backgroundColor: '#1e293b', border: '1px solid #3b82f6'}}>📁 INGEST FOLDER
            <input type="file" onChange={handleUpload} style={{display: 'none'}} webkitdirectory="true" directory="true" multiple />
          </label>
        </div>
      </div>

      {/* Telemetry Stats */}
      <div style={styles.stats}>
        <div style={styles.card}>INGESTED: {results.length}</div>
        <div style={{...styles.card, color: '#10b981'}}>VERIFIED: {results.filter(r => r.threatScore < 40).length}</div>
        <div style={{...styles.card, color: '#ef4444'}}>ANOMALIES: {results.filter(r => r.threatScore >= 70).length}</div>
      </div>

      {/* Interactive Terminal / Console */}
      {previewContent ? (
        <div style={{ backgroundColor: '#000', border: '1px solid #a855f7', padding: '15px', margin: '20px 0', borderRadius: '4px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
            <span style={{ color: '#a855f7' }}>[UNMASKED_DATA]: {previewContent.name}</span>
            <button onClick={() => setPreviewContent(null)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer' }}>CLOSE</button>
          </div>
          <pre style={{ color: '#f8fafc', fontSize: '12px', whiteSpace: 'pre-wrap', maxHeight: '150px', overflowY: 'auto' }}>
            {previewContent.data}
          </pre>
        </div>
      ) : (
        <PulseConsole lastResult={results[0]} scanning={scanning} />
      )}

      {/* Evidence Table */}
      <table style={styles.table}>
        <thead>
          <tr>
            <th style={styles.th}>IDENTIFIER</th>
            <th style={styles.th}>CLASS</th>
            <th style={styles.th}>ENTROPY</th>
            <th style={styles.th}>SHA-256_HASH</th>
            <th style={{...styles.th, textAlign: 'right'}}>INTEL_STATUS</th>
          </tr>
        </thead>
        <tbody>
          {results.length > 0 ? results.map((r, i) => (
            <tr key={i} className="threat-row">
              <td style={styles.td}>{r.fileName}</td>
              <td style={{...styles.td, color: '#60a5fa'}}>{r.detectedType}</td>
              <td style={styles.td}>{r.entropy.toFixed(2)}</td>
              <td style={{...styles.td, color: '#475569', fontSize: '10px'}}>{r.hash?.substring(0, 32)}...</td>
              <td style={{...styles.td, textAlign: 'right'}}>
                 <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end', alignItems: 'center' }}>
                    {r.threatScore >= 70 && (
                      <button 
                        className="unmask-btn"
                        onClick={() => handleUnmask(r.fileName)}
                        style={{ padding: '4px 8px', backgroundColor: '#6d28d9', color: 'white', border: 'none', borderRadius: '3px', fontSize: '10px', cursor: 'pointer', transition: '0.2s' }}
                      >
                        UNMASK
                      </button>
                    )}
                    <span style={{ color: r.threatScore >= 70 ? '#ef4444' : r.threatScore >= 40 ? '#fbbf24' : '#10b981', fontWeight: 'bold' }}>
                      {r.threatScore.toFixed(1)}%
                    </span>
                 </div>
              </td>
            </tr>
          )) : (
            <tr><td colSpan="5" style={{...styles.td, textAlign: 'center', color: '#475569', padding: '60px'}}>AWAITING_INGESTION...</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}