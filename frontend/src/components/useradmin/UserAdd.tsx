import * as React from "react";
import { Box, Button, Container, IconButton, TextField } from "@mui/material";
import Dialog from "@mui/material/Dialog";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";
import { TransitionProps } from "@mui/material/transitions";
import AddIcon from "@mui/icons-material/Add";
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

export default function UserAddDialog() {
  const [open, setOpen] = React.useState(false);

  const [firstNameInput, setFirstNameInput] = useState("");
  const [lastNameInput, setLastNameInput] = useState("");
  const [emailInput, setEmailInput] = useState("");
  const [vacationDaysInput, setVacationDaysInput] = useState(20);
  const [teamsInput, setTeamsInput] = useState("");

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSave = () => {
    (async () => {
      const response = await UserAdminService.createUser({
        firstName: firstNameInput,
        lastName: lastNameInput,
        email: emailInput,
        vacationDays: vacationDaysInput,
        teams: teamsInput.split(","),
      });
      //console.log(response);
      if (!response.ok) {
        EventBus.$dispatch("alert.show", {
          message: "ERROR: Creating user - " + response.statusText,
          severity: "error",
        });
      } else {
        setOpen(false);
        EventBus.$dispatch("alert.show", {
          message: "SUCCESS: User created.",
          severity: "success",
        });
      }
    })();
  };

  return (
    <span>
      <Button
        variant="contained"
        startIcon={<AddIcon />}
        onClick={handleClickOpen}
      >
        Add User
      </Button>
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
              Add User
            </Typography>
            <Button autoFocus color="inherit" onClick={handleSave}>
              save
            </Button>
          </Toolbar>
        </AppBar>
        {/* TODO - the form could be extracted into a new class, and reused in UserEdit */}
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
              onChange={(e) => {
                setFirstNameInput(e.target.value);
              }}
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
              onChange={(e) => {
                setLastNameInput(e.target.value);
              }}
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
              onChange={(e) => {
                setEmailInput(e.target.value);
              }}
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
              onChange={(e) => {
                setVacationDaysInput(parseInt(e.target.value, 10));
              }}
            />
            <TextField
              id="outlined-basic"
              label="Teams"
              variant="outlined"
              type="string"
              fullWidth
              margin="normal"
              defaultValue={teamsInput}
              onChange={(e) => {
                setTeamsInput(e.target.value);
              }}
            />
          </Box>
        </Container>
      </Dialog>
    </span>
  );
}
