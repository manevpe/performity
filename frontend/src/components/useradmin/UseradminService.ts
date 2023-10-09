import IUserData from "./IUserData";

interface UserAdminServiceProps extends IUserData {}

const UserAdminService = {
  getAllUsers: async () => {
    // TODO - get jwt from service
    const jwtToken = JSON.parse(sessionStorage.getItem("oidc.user:http://localhost:8180/realms/dundermifflin:performity") || "").access_token;
    const response = await fetch("/useradmin/v1/users", {
      headers: {
        Authorization: 'Bearer ' + jwtToken
      },
    });
    return response.json().then((data) => {
      //console.log(data);
      const parsedData: Array<IUserData> = [];
      let i = 0;
      // TODO - handle errors in a better way
      if (data.status === 500) return [];
      data.forEach((d: IUserData) => {
        parsedData.push({ id: i, ...d });
        i++;
      });
      return parsedData;
    });
  },

  updateUser: async (email: string, userData: UserAdminServiceProps) => {
    //console.log("Updating user with email " + email);
    //console.log(userData);
    // const csrfToken = document.cookie.replace(
    //   /(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/,
    //   "$1"
    // );
    //console.log(csrfToken);
    const jwtToken = JSON.parse(sessionStorage.getItem("oidc.user:http://localhost:8180/realms/dundermifflin:performity") || "").access_token;
    const response = await fetch("/useradmin/v1/users/" + email, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: 'Bearer ' + jwtToken
        //"X-XSRF-TOKEN": csrfToken,
      },
      body: JSON.stringify(userData),
    });
    //return response.json().then((data) => {});
    return response;
  },
};

export default UserAdminService;
