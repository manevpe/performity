import { createBrowserRouter, RouterProvider } from "react-router-dom";

import "./App.css";
import Root from "./routes/root";
import ErrorPage from "./routes/error-page";
import UseradminPage from "./routes/useradmin";
import HomePage from "./routes/home";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root></Root>,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "",
        element: <HomePage />,
      },
      {
        path: "useradmin",
        element: <UseradminPage />,
      },
    ],
  },
]);

function App(): JSX.Element {
  return (
    <div className="App">
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
