import './App.css';
import Button from '@mui/material/Button';

function handleClick() {
  fetch("/useradmin/v1/users").then((response) => {
    response.json().then((data) => {
        console.log(data);
    });
  });
};

function App(): JSX.Element {
  return (
    <div className="App">
      <Button variant="contained" onClick={handleClick}>Hello world</Button>
    </div>
  );
}

export default App;
