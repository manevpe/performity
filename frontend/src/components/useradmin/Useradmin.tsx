import { Container, IconButton } from "@mui/material";
import { useEffect } from "react";
import { DataGrid, GridRowsProp, GridColDef } from "@mui/x-data-grid";
import React from "react";
import DeleteIcon from "@mui/icons-material/Delete";
import UserEdit from "./UserEdit";
import UserAdd from "./UserAdd";
import UserAdminService from "./UseradminService";
import UserDelete from "./UserDelete";

const Useradmin = () => {
  const [users, setUsers] = React.useState<GridRowsProp>([]);

  useEffect(() => {
    (async () => {
      const data = await UserAdminService.getAllUsers();
      setUsers(data);
    })();
  }, []);

  const columns: GridColDef[] = [
    {
      field: "fullName",
      headerName: "Name",
      flex: 3,
      valueGetter: (params) => {
        return `${params.row.firstName || ""} ${params.row.lastName || ""}`;
      },
    },
    { field: "email", headerName: "Email", flex: 5 },
    { field: "vacationDays", headerName: "Vacation Days", flex: 3 },
    { field: "teams", headerName: "Teams", width: 150, flex: 5 },
    {
      field: "actions",
      headerName: "",
      renderCell: (cell) => (
        <strong>
          <UserEdit rowData={cell.row}></UserEdit>
          <UserDelete email={cell.row.email}></UserDelete>
        </strong>
      ),
      sortable: false,
      disableColumnMenu: true,
    },
    // align: "center", headerAlign: "center",
  ];

  const [deleteModalOpen, setdeleteModalOpen] = React.useState(false);

  const handleDeleteModalClose = () => {
    setdeleteModalOpen(false);
  };

  return (
    <Container>
      <div id="useradmin">
        This is the useradmin page.
        <br />
        <UserAdd></UserAdd>
        <DataGrid rows={users} columns={columns} disableRowSelectionOnClick />
      </div>
    </Container>
  );
};

export default Useradmin;
