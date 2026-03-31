import { useState } from "react";
import "./Quote.css"; // Importing CSS for styling

const Quote = () => {
    const [response, setResponse] = useState({
        "സ്രഷ്ടാവ്": "പുലി മുരുകൻ",
        "എണ്ണിയ മൂല്യം": 0,
        "ഉദ്ധരണി": "ഉദ്ധരണി കൊണ്ടുവരാൻ തുടങ്ങിയിട്ടില്ല"
    });

    const fetchAdvice = async () => {
        try {
            const res = await fetch("/api/v1/quote/random");
            const json = await res.json();
            setResponse(json); // Corrected to update state with API response
        } catch (error) {
            console.error("Error fetching data:", error);
            setResponse({ പിശക് : "ഡാറ്റ ശേഖരിക്കുന്നതിൽ പരാജയപ്പെട്ടു" });
        }
    };

    return (
        <div className="quote-container">
            <h1>മലയാളം ഉദ്ധരണി ജനറേറ്റർ</h1>
            <table className="quote-table">
                <tbody>
                    {Object.entries(response).map(([key, value], index) => (
                        <tr key={index}>
                            <td className="quote-key"><strong>{key}</strong></td>
                            <td className="quote-value">{value}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button className="quote-button" onClick={fetchAdvice}>Get Advice</button>
        </div>
    );
}

export default Quote;
