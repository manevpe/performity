import { Outlet, useNavigation } from "react-router-dom";
import Layout from "../components/Layout";
import AlertCustom from "../components/AlertCustom";

export default function Root() {
  const navigation = useNavigation();

  return (
    <>
      <div
        id="detail"
        className={navigation.state === "loading" ? "loading" : ""}
      >
        <Layout></Layout>
        <Outlet />
        <AlertCustom></AlertCustom>
      </div>
    </>
  );
}
