import React from "react";

export default function FloorPlanOverlay({ floor, onClose }) {
  return (
    <div
      style={{
        position: "absolute",
        top: 0, left: 0, right: 0, bottom: 0,
        background: "rgba(255,255,255,0.97)",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        zIndex: 2000
      }}
    >
      <h2 style={{ marginBottom: 20 }}>
        Sơ đồ mặt bằng - {floor === 0 ? "Tầng trệt" : `Tầng ${floor}`}
      </h2>

      <div
        style={{
          width: 820,
          height: 460,
          background: "#f3f4f6",
          border: "2px solid #ccc",
          position: "relative",
          boxShadow: "0 6px 18px rgba(0,0,0,0.12)"
        }}
      >
        {floor === 0 ? <GroundFloorPlan /> : <ApartmentFloorPlan floor={floor} />}
      </div>

      <button
        onClick={onClose}
        style={{
          marginTop: 18,
          padding: "8px 16px",
          fontWeight: "700",
          borderRadius: 6,
          border: "none",
          cursor: "pointer",
          background: "#111827",
          color: "white"
        }}
      >
        Đóng
      </button>
    </div>
  );
}

/* ---------- GROUND FLOOR (with Stair added) --------- */
function GroundFloorPlan() {
  return (
    <>
      {/* Sảnh chính ngang giữa */}
      <div style={{
        position: "absolute",
        left: "5%",
        top: "42%",
        width: "90%",
        height: "16%",
        background: "#e5e7eb",
        border: "1px dashed #9ca3af",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontWeight: 700
      }}>
        Sảnh chính / Lối đi
      </div>

      {/* Top row: Ba phòng lớn */}
      <Box left="5%" top="6%" w="28%" h="32%" bg="#fde68a">Phòng Quản trị</Box>
      <Box left="36%" top="6%" w="28%" h="32%" bg="#93c5fd">Phòng Giám đốc</Box>
      <Box left="67%" top="6%" w="28%" h="32%" bg="#fca5a5">Phòng Kế toán</Box>

      {/* Bottom row: Cầu thang (trái), Thang máy (giữa), WC (phải) */}
      <Box left="8%" top="62%" w="18%" h="26%" bg="#fca5a5" bold>Thang bộ</Box>
      <Box left="41%" top="62%" w="18%" h="26%" bg="#fde68a" bold>Thang máy</Box>
      <Box left="74%" top="62%" w="18%" h="26%" bg="#bbf7d0" bold>WC chung</Box>

      {/* Một số chú thích nhỏ */}
      <div style={{ position: "absolute", left: 12, bottom: 8, fontSize: 12, color: "#374151" }}>
        Chú ý: vị trí hiển thị tương đối — bạn có thể yêu cầu tinh chỉnh tọa độ nếu cần.
      </div>
    </>
  );
}

/* ---------- APARTMENT FLOOR (10 căn) ---------- */
function ApartmentFloorPlan({ floor }) {
  const apartments = Array.from({ length: 10 }, (_, i) => `Căn ${floor}${String(i + 1).padStart(2, "0")}`);

  return (
    <>
      {/* Hành lang ở giữa */}
      <div style={{
        position: "absolute",
        top: "44%",
        left: "5%",
        right: "5%",
        height: "12%",
        background: "#e5e7eb",
        border: "1px dashed #9ca3af",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontWeight: 700,
        fontSize: 14
      }}>
        Hành lang
      </div>

      {/* Căn hộ - chia thành 2 dãy 5 trên 1 dãy */}
      {apartments.map((apt, i) => {
        const col = i % 5;
        const row = i < 5 ? 0 : 1;
        const left = 5 + col * 18; // 18% step
        const top = row === 0 ? 6 : 60; // top position per row
        return (
          <div key={i} style={{
            position: "absolute",
            left: `${left}%`,
            top: `${top}%`,
            width: "16%",
            height: "30%",
            border: "1px solid #9ca3af",
            background: "#d9f99d",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            fontWeight: 700,
            fontSize: 13
          }}>
            {apt}
          </div>
        );
      })}

      {/* Thang bộ (trái), Thang máy (giữa phải), WC (gần thang máy) */}
      <Box left="5%" top="40%" w="12%" h="18%" bg="#fca5a5" bold>Thang bộ</Box>
      <Box left="78%" top="40%" w="12%" h="18%" bg="#fde68a" bold>Thang máy</Box>
      <Box left="62%" top="42%" w="12%" h="14%" bg="#bbf7d0" bold>WC</Box>
    </>
  );
}

/* ---------- Small Box helper ---------- */
function Box({ left, top, w, h, bg = "#fff", children, bold }) {
  return (
    <div style={{
      position: "absolute",
      left,
      top,
      width: w,
      height: h,
      background: bg,
      border: "1px solid #999",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      fontWeight: bold ? 800 : 700,
      fontSize: 14,
      textAlign: "center",
      padding: 6,
      boxSizing: "border-box"
    }}>
      {children}
    </div>
  );
}
