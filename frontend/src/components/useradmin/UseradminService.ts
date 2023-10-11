import AuthService from "../../utils/auth";
import IUserData from "./IUserData";

interface UserAdminServiceProps extends IUserData {}

const UserAdminService = {
  getAllUsers: async () => {
    const jwtToken = AuthService.getJwtToken().access_token;
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

  getUserDetails: async (email: string) => {
    const jwtToken = AuthService.getJwtToken().access_token;
    const response = await fetch("/useradmin/v1/users/" + email, {
      headers: {
        Authorization: 'Bearer ' + jwtToken
      },
    });
    return response.json().then((data) => {
      //console.log(data);
      return data;
    });
  },

  createUser: async (userData: UserAdminServiceProps) => {
    const jwtToken = AuthService.getJwtToken().access_token;
    const response = await fetch("/useradmin/v1/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: 'Bearer ' + jwtToken
      },
      body: JSON.stringify(userData),
    });
    return response;
  },

  updateUser: async (email: string, userData: UserAdminServiceProps) => {
    const jwtToken = AuthService.getJwtToken().access_token;
    const response = await fetch("/useradmin/v1/users/" + email, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: 'Bearer ' + jwtToken
      },
      body: JSON.stringify(userData),
    });
    return response;
  },

  // TODO - patch request

  deleteUser: async (email: string) => {
    const jwtToken = AuthService.getJwtToken().access_token;
    const response = await fetch("/useradmin/v1/users/" + email, {
      method: "DELETE",
      headers: {
        Authorization: 'Bearer ' + jwtToken
      },
    });
    // TODO - return success or failure
    return response;
  },
};

export default UserAdminService;
