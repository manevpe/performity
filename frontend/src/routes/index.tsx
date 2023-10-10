import { createBrowserRouter } from "react-router-dom";
import Root from "./root";
import ErrorPage from "./error-page";
import UseradminPage from "./useradmin";
import HomePage from "./home";

const RouterIndex = createBrowserRouter([
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

export default RouterIndex;