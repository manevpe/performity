import { useEffect, useState } from "react";
import { hasAuthParams, useAuth } from "react-oidc-context";
import EventBus from "../utils/event_bus";
import { User } from "oidc-client-ts";

function getUser() {
  const sessionKey =
    "oidc.user:" +
    process.env.REACT_APP_OAUTH2_SERVER_URL +
    "/realms/" +
    process.env.REACT_APP_OAUTH2_REALM +
    ":" +
    process.env.REACT_APP_OAUTH2_CLIENT_ID;
  const oidcStorage = sessionStorage.getItem(sessionKey);
  if (!oidcStorage) {
    return null;
  }

  return User.fromStorageString(oidcStorage);
}

export default function Root() {
  const auth = useAuth();

  const [hasTriedSignin, setHasTriedSignin] = useState(false);

  // automatically sign-in
  useEffect(() => {
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

  useEffect(() => {
    // the `return` is important - addAccessTokenExpiring() returns a cleanup function
    return auth.events.addAccessTokenExpiring(() => {
      // TODO - replace with customalert
      // alert(
      //   "You're about to be signed out due to inactivity. Press continue to stay signed in."
      // );
      auth.signinSilent();
    });
  }, [auth, auth.events, auth.signinSilent]);

  switch (auth.activeNavigator) {
    case "signinSilent":
      setTimeout(
        () =>
          EventBus.$dispatch("alert.show", {
            message: "Signing in...",
            severity: "info",
          }),
        0
      );
      break;
    case "signoutRedirect":
      setTimeout(
        () =>
          EventBus.$dispatch("alert.show", {
            message: "Signing out...",
            severity: "info",
          }),
        0
      );
      break;
  }

  // if (auth.isLoading) {
  //   setTimeout(() => EventBus.$dispatch("alert.show", { message: "Authentication loading...", severity: "info" }), 0);
  // }

  // if (!auth.isAuthenticated) {
  //   setTimeout(() => EventBus.$dispatch("alert.show", { message: "ERROR: Unable to sign in", severity: "error" }), 0);
  // }

  // if (auth.error) {
  //   setTimeout(() => EventBus.$dispatch("alert.show", { message: "ERROR: Unable to sign in - " + auth.error.message, severity: "error" }), 0);
  // }

  // if (auth.isAuthenticated) {
  //   setTimeout(
  //     () =>
  //       EventBus.$dispatch("alert.show", {
  //         message: "Signed in as " + getUser()?.profile.email,
  //         severity: "success",
  //       }),
  //     0
  //   );
  // }

  return <></>;
}
