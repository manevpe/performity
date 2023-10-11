const AuthService = {
  getJwtToken: () => {
    //const [sessionKey, sessionValue] = Object.entries(sessionStorage).find(([k,v])=> k.startsWith('oidc.user'));
    const sessionKey =
      "oidc.user:" +
      process.env.REACT_APP_OAUTH2_SERVER_URL +
      "/realms/" +
      process.env.REACT_APP_OAUTH2_REALM +
      ":" +
      process.env.REACT_APP_OAUTH2_CLIENT_ID;
    const sessionValue = sessionStorage.getItem(sessionKey) || "{}";
    return JSON.parse(sessionValue);
  },

  getCsrfToken: () => {
    const csrfToken = document.cookie.replace(
      /(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/,
      "$1"
    );
    //console.log(csrfToken);
    return csrfToken;
  },
};

export default AuthService;
