import { createBrowserRouter, RouterProvider } from "react-router-dom";

import "./App.css";
import Root from "./routes/root";
import ErrorPage from "./routes/error-page";
import UseradminPage from "./routes/useradmin";
import HomePage from "./routes/home";
import { useAuth, hasAuthParams } from "react-oidc-context";
import React, { useState } from "react";

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
  const auth = useAuth();

  const [hasTriedSignin, setHasTriedSignin] = useState(false);

  // automatically sign-in
  React.useEffect(() => {
    if (
      !hasAuthParams() &&
      !auth.isAuthenticated &&
      !auth.activeNavigator &&
      !auth.isLoading &&
      !hasTriedSignin
    ) {
      auth.signinRedirect();
      setHasTriedSignin(true);
    }
  }, [auth, hasTriedSignin]);

  React.useEffect(() => {
    // the `return` is important - addAccessTokenExpiring() returns a cleanup function
    return auth.events.addAccessTokenExpiring(() => {
      alert(
        "You're about to be signed out due to inactivity. Press continue to stay signed in."
      );
      auth.signinSilent();
    });
  }, [auth, auth.events, auth.signinSilent]);

  if (auth.isLoading) {
    return <div>Signing you in/out...</div>;
  }

  if (!auth.isAuthenticated) {
    return <div>Unable to log in</div>;
  }

  // switch (auth.activeNavigator) {
  //   case "signinSilent":
  //     return <div>Signing you in...</div>;
  //   case "signoutRedirect":
  //     return <div>Signing you out...</div>;
  // }

  // if (auth.isLoading) {
  //   return <div>Loading...</div>;
  // }

  // if (auth.error) {
  //   return <div>Oops... {auth.error.message}</div>;
  // }

  // if (auth.isAuthenticated) {
  //   return (
  //     <div>
  //       Hello {auth.user?.profile.sub}{" "}
  //       <button onClick={() => void auth.removeUser()}>Log out</button>
  //     </div>
  //   );
  // }

  // return <button onClick={() => void auth.signinRedirect()}>Log in</button>;

  return (
    <div className="App">
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
