import IUserData from "./IUserData";

interface UserAdminServiceProps extends IUserData {}

const UserAdminService = {
  getAllUsers: async () => {
    const response = await fetch("/useradmin/v1/users");
    return response.json().then((data) => {
      //console.log(data);
      const parsedData: Array<IUserData> = [];
      let i = 0;
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
    const csrfToken = document.cookie.replace(
      /(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/,
      "$1"
    );
    //console.log(csrfToken);
    const response = await fetch("/useradmin/v1/users/" + email, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "X-XSRF-TOKEN": csrfToken,
      },
      body: JSON.stringify(userData),
    });
    //return response.json().then((data) => {});
    return response;
  },
};

export default UserAdminService;
