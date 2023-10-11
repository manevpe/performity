import * as React from "react";
import { Button, DialogActions, DialogContent, DialogContentText, DialogTitle, IconButton } from "@mui/material";
import Dialog from "@mui/material/Dialog";
import DeleteIcon from "@mui/icons-material/Delete";
import UserAdminService from "./UseradminService";
import EventBus from "../../utils/event_bus";

export default function UserDeleteDialog(props: { email: string }) {
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleConfirm = () => {
    (async () => {
      const response = await UserAdminService.deleteUser(props.email);
      //console.log(response);
      if (!response.ok) {
        EventBus.$dispatch("alert.show", { message: "ERROR: Deleting user failed - " + response.statusText, severity: "error" });
      } else {
        setOpen(false);
        EventBus.$dispatch("alert.show", { message: "SUCCESS: User deleted.", severity: "success" });
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
        <DeleteIcon />
      </IconButton>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Delete user " + props.email + "?"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-delete-user">
            Are you sure you want to delete this user?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleConfirm} autoFocus color="error" variant="contained">
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </span>
  );
}
