import { useLocation } from "react-router";
import "./Header.css";

export default function Header() {
  const path = useLocation().pathname;
  let siteName;
  if (path === "/api") {
    siteName = "Chronicle API documentation";
  } else {
    siteName = "Chronicle";
  }

  return (
    <header>
      <h1>Welcome to {siteName}</h1>
    </header>
  );
}
