import React from "react";
import { Navbar, Nav, Container, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const HeaderHome = () => {
  const navigate = useNavigate();

  return (
    <Navbar expand="lg" bg="light" fixed="top" className="shadow-sm py-3">
      <Container>
        <Navbar.Brand
          onClick={() => navigate("/")}
          className="fw-bold fs-4 text-primary"
          style={{ cursor: "pointer" }}
        >
          SUNHOUSE APARTMENT
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto gap-4 align-items-center">
            <Nav.Link onClick={() => navigate("/home")}>HOME</Nav.Link>
            <Nav.Link onClick={() => navigate("/about")}>ABOUT</Nav.Link>
            <Nav.Link onClick={() => navigate("/contact")}>CONTACT</Nav.Link>
            <Button variant="outline-primary" onClick={() => navigate("/login")}>
              LOGIN
            </Button>
            <Nav.Link onClick={() => navigate("/apartment")}>SƠ ĐỒ CHUNG CƯ</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default HeaderHome;
