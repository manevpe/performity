import { Button } from "@mui/material";
import { useEffect } from "react";
import { DataGrid, GridRowsProp, GridColDef } from '@mui/x-data-grid';
import React from "react";

function handleClick() {}

interface UserData {
  email: string;
  firstName: string;
  lastName: string;
  vacationDays: number;
  teams: Array<string>;
}

interface UserDataRow extends UserData {
  id: number;
}

const Useradmin = () => {
  const [users, setUsers] = React.useState<GridRowsProp>([]);
  
  useEffect(() => {
    fetch("/useradmin/v1/users").then((response) => {
      response.json().then((data) => {
        console.log(data);
        const parsedData: Array<UserDataRow> = [];
        let i = 0;
        data.forEach((d: UserData) => {
          parsedData.push({ 'id': i, ...d });
          i++;
        })
        setUsers(parsedData);
      });
    });
  }, []);

  const columns: GridColDef[] = [
    { field: 'firstName', headerName: 'First Name', width: 150 },
    { field: 'lastName', headerName: 'Last Name', width: 150 },
    { field: 'email', headerName: 'Email', width: 150 },
    { field: 'vacationDays', headerName: 'Vacation Days', width: 150 },
    { field: 'teams', headerName: 'Teams', width: 150 },
  ];

  return (
    <div id="useradmin">
      This is the useradmin page.
      <br />
      <Button variant="contained" onClick={handleClick}>
        Hello world
      </Button>
      <br />
      <DataGrid rows={users} columns={columns} />
    </div>
  );
};

export default Useradmin;
