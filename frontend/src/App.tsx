import { RouterProvider } from "react-router-dom";
import "./App.css";
import RouterIndex from "./routes/index";

const router = RouterIndex;

function App(): JSX.Element {
  return (
    <div className="App">
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
