import React from "react";
import ApartmentSimulator3D from "../components/ApartmentSimulator3D";

export default function ApartmentPage() {
  return (
    <div style={{ marginTop: "80px" }}> 
      {/* marginTop để không bị đè bởi Navbar */}
      <ApartmentSimulator3D />
    </div>
  );
}
