const AuthService = {
  getJwtToken: () => {
    const sessionKey = Object.entries(sessionStorage).find(([k,v])=> k.startsWith('oidc.user'))?.toString();
    if (!sessionKey) return "";
    return JSON.parse(sessionStorage.getItem(sessionKey) || "");
  },

  getCsrfToken: () => {
    const csrfToken = document.cookie.replace(
      /(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/,
      "$1"
    );
    console.log(csrfToken);
    return csrfToken;
  },
};

export default AuthService;
