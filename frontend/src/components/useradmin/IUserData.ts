export default interface IUserData {
  email: string;
  firstName: string;
  lastName: string;
  vacationDays: number;
  teams: Array<string>;
  id?: number;
}
