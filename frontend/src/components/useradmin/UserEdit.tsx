import * as React from "react";
import { Box, Button, Container, IconButton, Snackbar, TextField } from "@mui/material";
import Dialog from "@mui/material/Dialog";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";
import { TransitionProps } from "@mui/material/transitions";
import EditIcon from "@mui/icons-material/Edit";
import IUserData from "./IUserData";
import UserAdminService from "./UseradminService";
import { useState } from "react";
import EventBus from "../../utils/event_bus";

const Transition = React.forwardRef(function Transition(
  props: TransitionProps & {
    children: React.ReactElement;
  },
  ref: React.Ref<unknown>
) {
  return <Slide direction="up" ref={ref} {...props} />;
});

export default function UserEditDialog(props: { rowData: IUserData }) {
  const [open, setOpen] = React.useState(false);
  //console.log(props.rowData);

  const [firstNameInput, setFirstNameInput] = useState(props.rowData.firstName);
  const [lastNameInput, setLastNameInput] = useState(props.rowData.lastName);
  const [emailInput, setEmailInput] = useState(props.rowData.email);
  const [vacationDaysInput, setVacationDaysInput] = useState(
    props.rowData.vacationDays
  );
  const [teamsInput, setTeamsInput] = useState(props.rowData.teams);

  const handleFirstNameChange = (event: { target: { value: any } }) => {
    setFirstNameInput(event.target.value);
  };

  const handleLastNameChange = (event: { target: { value: any } }) => {
    setLastNameInput(event.target.value);
  };

  const handleEmailChange = (event: { target: { value: any } }) => {
    setEmailInput(event.target.value);
  };

  const handleVacationDaysChange = (event: { target: { value: any } }) => {
    setVacationDaysInput(event.target.value);
  };

  const handleTeamsChange = (event: { target: { value: any } }) => {
    setTeamsInput(event.target.value);
  };

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setFirstNameInput(props.rowData.firstName);
    setLastNameInput(props.rowData.lastName);
    setEmailInput(props.rowData.email);
    setVacationDaysInput(props.rowData.vacationDays);
    setTeamsInput(props.rowData.teams);
    setOpen(false);
  };

  const handleSave = () => {
    (async () => {
      const response = await UserAdminService.updateUser(props.rowData.email, {
        firstName: firstNameInput,
        lastName: lastNameInput,
        email: emailInput,
        vacationDays: vacationDaysInput,
        teams: teamsInput,
      });
      //console.log(response);
      if (!response.ok) {
        EventBus.$dispatch("alert.show", { message: "ERROR: " + response.statusText, severity: "error" });
      } else {
        setOpen(false);
        EventBus.$dispatch("alert.show", { message: "SUCCESS: User updated.", severity: "success" });
      }
    })();
  };

  return (
    <span>
      <IconButton
        aria-label="edit"
        onClick={() => {
          handleClickOpen();
        }}
      >
        <EditIcon />
      </IconButton>
      <Dialog
        fullScreen
        open={open}
        onClose={handleClose}
        TransitionComponent={Transition}
      >
        <AppBar sx={{ position: "relative" }}>
          <Toolbar>
            <IconButton
              edge="start"
              color="inherit"
              onClick={handleClose}
              aria-label="close"
            >
              <CloseIcon />
            </IconButton>
            <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
              Edit {props.rowData.firstName} {props.rowData.lastName}
            </Typography>
            <Button autoFocus color="inherit" onClick={handleSave}>
              save
            </Button>
          </Toolbar>
        </AppBar>
        <Container maxWidth="sm">
          <Box
            component="form"
            // sx={{
            //   "& > :not(style)": { m: 1, width: "25ch" },
            // }}
            noValidate
            autoComplete="off"
          >
            <TextField
              id="outlined-basic"
              label="First Name"
              variant="outlined"
              required
              type="string"
              fullWidth
              margin="normal"
              defaultValue={firstNameInput}
              onChange={handleFirstNameChange}
            />
            <TextField
              id="outlined-basic"
              label="Last Name"
              variant="outlined"
              required
              type="string"
              fullWidth
              margin="normal"
              defaultValue={lastNameInput}
              onChange={handleLastNameChange}
            />
            <TextField
              id="outlined-basic"
              label="Email"
              variant="outlined"
              required
              type="string"
              fullWidth
              margin="normal"
              defaultValue={emailInput}
              onChange={handleEmailChange}
            />
            <TextField
              id="outlined-basic"
              label="Vacation Days"
              variant="outlined"
              required
              type="number"
              fullWidth
              margin="normal"
              defaultValue={vacationDaysInput}
              onChange={handleVacationDaysChange}
            />
            <TextField
              id="outlined-basic"
              label="Teams"
              variant="outlined"
              required
              type="string"
              fullWidth
              margin="normal"
              defaultValue={teamsInput}
              onChange={handleTeamsChange}
            />
          </Box>
        </Container>
      </Dialog>
    </span>
  );
}
