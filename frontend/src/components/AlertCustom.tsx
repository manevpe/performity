import * as React from "react";
import Stack from "@mui/material/Stack";
import Button from "@mui/material/Button";
import Snackbar from "@mui/material/Snackbar";
import MuiAlert, { AlertColor, AlertProps } from "@mui/material/Alert";
import EventBus from "../utils/event_bus";

const Alert = React.forwardRef<HTMLDivElement, AlertProps>(function Alert(
  props,
  ref
) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function AlertCustom() {
  const [alertOpen, setAlertOpen] = React.useState(false);
  const [alertSeverity, setAlertSeverity] =
    React.useState<AlertColor>("success");
  const [alertMessage, setAlertMessage] = React.useState("");
  const [alertTimeout, setAlertTimeout] = React.useState(6000);

  EventBus.$on('alert.show', (data) => showAlert(data.detail.severity, data.detail.message, data.detail.timeout));


  const showAlert = (severity: AlertColor, message: string, timeoutValue: number = 6000) => {
    setAlertSeverity(severity);
    setAlertMessage(message);
    setAlertOpen(true);
    setAlertTimeout(timeoutValue);
  };

  const handleClick = () => {
    setAlertOpen(true);
  };

  const handleClose = (
    event?: React.SyntheticEvent | Event,
    reason?: string
  ) => {
    if (reason === "clickaway") {
      return;
    }
    setAlertOpen(false);
  };

  return (
    <Stack spacing={2} sx={{ width: "100%" }}>
      <Snackbar key={alertMessage} open={alertOpen} autoHideDuration={alertTimeout} onClose={handleClose}>
        <Alert onClose={handleClose} severity={alertSeverity} sx={{ width: "100%" }}>
          {alertMessage}
        </Alert>
      </Snackbar>
    </Stack>
  );
}
