import React, { useMemo, useRef, useState, useEffect } from "react";
import * as THREE from "three";
import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls, Environment, Text, StatsGl, Float } from "@react-three/drei";
import FloorPlanOverlay from "../components/FloorPlanOverlay"

// ====== CONSTANTS ======
const FLOORS = 10;
const GRID_X = 5;
const GRID_Z = 2;

const APT_W = 4;
const APT_H = 2.8;
const APT_D = 6;
const GAP = 0.4;

const FLOOR_HEIGHT = APT_H + 0.2;

// ====== HELPERS ======
function useApartmentMatrices() {
  return useMemo(() => {
    const matrices = [];
    const dummy = new THREE.Object3D();
    const startX = -((GRID_X * (APT_W + GAP) - GAP) / 2) + APT_W / 2;
    const startZ = -((GRID_Z * (APT_D + GAP) - GAP) / 2) + APT_D / 2;

    for (let floor = 0; floor < FLOORS; floor++) {
      for (let iz = 0; iz < GRID_Z; iz++) {
        for (let ix = 0; ix < GRID_X; ix++) {
          const x = startX + ix * (APT_W + GAP);
          const y = floor * FLOOR_HEIGHT + APT_H / 2;
          const z = startZ + iz * (APT_D + GAP);
          dummy.position.set(x, y, z);
          dummy.updateMatrix();
          matrices.push(dummy.matrix.clone());
        }
      }
    }
    return matrices;
  }, []);
}

function Apartments({ color = "#d9e3f0" }) {
  const ref = useRef();
  const matrices = useApartmentMatrices();

  useEffect(() => {
    if (ref.current) {
      matrices.forEach((m, i) => ref.current.setMatrixAt(i, m));
      ref.current.instanceMatrix.needsUpdate = true;
    }
  }, [matrices]);

  return (
    <instancedMesh ref={ref} args={[null, null, matrices.length]} castShadow receiveShadow>
      <boxGeometry args={[APT_W, APT_H, APT_D]} />
      <meshStandardMaterial metalness={0.05} roughness={0.8} color={color} />
    </instancedMesh>
  );
}

function CoreShaft() {
  const width = 2.2;
  const depth = GRID_Z * (APT_D + GAP) - GAP + 2;
  const height = FLOORS * FLOOR_HEIGHT + 0.4;
  return (
    <mesh position={[0, height / 2 - 0.2, 0]} castShadow receiveShadow>
      <boxGeometry args={[width, height, depth]} />
      <meshStandardMaterial color="#b0c4de" roughness={0.7} />
    </mesh>
  );
}

function FloorSlabs({ onSelectFloor, selectedFloor }) {
  const slabs = [];
  const totalWidth = GRID_X * (APT_W + GAP) - GAP + 0.6;
  const totalDepth = GRID_Z * (APT_D + GAP) - GAP + 0.6;
  for (let f = 0; f < FLOORS; f++) {
    slabs.push(
      <mesh
        key={f}
        position={[0, f * FLOOR_HEIGHT, 0]}
        receiveShadow
        castShadow
        onClick={(e) => {
          // prevent click through when overlaying controls etc
          e.stopPropagation();
          onSelectFloor(f);
        }}
      >
        <boxGeometry args={[totalWidth, 0.2, totalDepth]} />
        <meshStandardMaterial
          color={selectedFloor === f ? "#60a5fa" : "#eeeeee"}
          transparent
          opacity={selectedFloor === f ? 0.9 : 0.6}
        />
      </mesh>
    );
  }
  return <group>{slabs}</group>;
}

// Ground plane, trees, etc
function Pool({ highlight }) {
  const size = [16, 8];
  const pos = [-(GRID_X * (APT_W + GAP)), 0.05, -14];
  return (
    <group position={pos}>
      <mesh rotation={[-Math.PI / 2, 0, 0]} receiveShadow>
        <planeGeometry args={[size[0] + 1, size[1] + 1]} />
        <meshStandardMaterial color={highlight ? "#00aaff" : "#9adcf7"} />
      </mesh>
      <mesh position={[0, 0.3, 0]} castShadow>
        <boxGeometry args={[size[0], 0.5, size[1]]} />
        <meshStandardMaterial color={highlight ? "#2fb3ff" : "#47b6ff"} transparent opacity={0.85} />
      </mesh>
      <Text position={[0, 0.8, size[1] / 2 + 1.2]} fontSize={0.8} color={highlight ? "#0077cc" : "#333"}>
        Hồ bơi
      </Text>
    </group>
  );
}
function Gym({ highlight }) {
  const size = [10, 4, 6];
  const pos = [GRID_X * (APT_W + GAP) + 8, size[1] / 2, -10];
  return (
    <group position={pos}>
      <mesh castShadow receiveShadow>
        <boxGeometry args={size} />
        <meshStandardMaterial color={highlight ? "#ffb703" : "#e6c88f"} />
      </mesh>
      <Text position={[0, size[1] + 0.2, 0]} fontSize={0.8} color="#333">
        Phòng gym
      </Text>
    </group>
  );
}
function SecurityGate({ highlight }) {
  const size = [6, 2.6, 4];
  const pos = [0, size[1] / 2, 18];
  return (
    <group position={pos}>
      <mesh castShadow receiveShadow>
        <boxGeometry args={size} />
        <meshStandardMaterial color={highlight ? "#ef4444" : "#c4c4c4"} />
      </mesh>
      <Text position={[0, size[1] + 0.2, 0]} fontSize={0.7} color="#333">
        Khu bảo vệ
      </Text>
    </group>
  );
}
function Tree({ position = [0, 0, 0] }) {
  return (
    <group position={position}>
      <mesh position={[0, 1, 0]} castShadow>
        <cylinderGeometry args={[0.2, 0.2, 2, 8]} />
        <meshStandardMaterial color="#8b5a2b" />
      </mesh>
      <Float speed={1.2} rotationIntensity={0.2} floatIntensity={0.6} floatingRange={[0.05, 0.15]}>
        <mesh position={[0, 2.2, 0]} castShadow>
          <icosahedronGeometry args={[1.2, 0]} />
          <meshStandardMaterial color="#22c55e" roughness={0.8} />
        </mesh>
      </Float>
    </group>
  );
}
function Park({ highlight }) {
  const width = 22;
  const depth = 14;
  const pos = [-(GRID_X * (APT_W + GAP)), 0.01, 8];

  const trees = useMemo(() => {
    const list = [];
    for (let i = 0; i < 14; i++) {
      const x = (Math.random() - 0.5) * (width - 4);
      const z = (Math.random() - 0.5) * (depth - 4);
      list.push([x, 0, z]);
    }
    return list;
  }, []);

  return (
    <group position={pos}>
      <mesh rotation={[-Math.PI / 2, 0, 0]} receiveShadow>
        <planeGeometry args={[width, depth]} />
        <meshStandardMaterial color={highlight ? "#86efac" : "#65a30d"} />
      </mesh>
      {trees.map((p, i) => (
        <Tree key={i} position={[p[0], 0, p[2]]} />
      ))}
      <Text position={[0, 0.4, depth / 2 + 1]} fontSize={0.7} color="#1f2937">
        Công viên
      </Text>
    </group>
  );
}
function Ground() {
  return (
    <mesh rotation={[-Math.PI / 2, 0, 0]} position={[0, 0, 0]} receiveShadow>
      <planeGeometry args={[120, 120]} />
      <meshStandardMaterial color="#e5e7eb" />
    </mesh>
  );
}
function SunLight() {
  const lightRef = useRef();
  useFrame((state) => {
    const t = state.clock.getElapsedTime();
    if (lightRef.current) {
      lightRef.current.position.set(Math.sin(t / 8) * 40, 50, Math.cos(t / 8) * 40);
      lightRef.current.target.position.set(0, 15, 0);
      lightRef.current.target.updateMatrixWorld();
    }
  });
  return (
    <group>
      <directionalLight ref={lightRef} castShadow intensity={2} shadow-mapSize-width={2048} shadow-mapSize-height={2048}>
        <orthographicCamera attach="shadow-camera" args={[-60, 60, 60, -60, 1, 200]} />
      </directionalLight>
      <hemisphereLight intensity={0.6} />
    </group>
  );
}

// ====== FLOOR PLAN 3D (TOP-DOWN) ======


// ====== GROUND FLOOR PLAN 3D (special rooms) ======


// ====== PATHWAYS (outside) - kept for 3D scene context ======
function Pathways() {
  const paths = [];

  // Đường chính từ cổng bảo vệ -> tòa nhà
  paths.push(
    <mesh key="gate-building" rotation={[-Math.PI / 2, 0, 0]} position={[0, 0.02, 10]}>
      <planeGeometry args={[4, 18]} />
      <meshStandardMaterial color="#d1d5db" />
    </mesh>
  );

  // Đường from sảnh sang hồ bơi
  paths.push(
    <mesh key="building-pool" rotation={[-Math.PI / 2, 0, 0]} position={[-12, 0.02, -6]}>
      <planeGeometry args={[6, 14]} />
      <meshStandardMaterial color="#d1d5db" />
    </mesh>
  );

  // Đường from sảnh sang công viên
  paths.push(
    <mesh key="building-park" rotation={[-Math.PI / 2, 0, 0]} position={[-12, 0.02, 8]}>
      <planeGeometry args={[6, 12]} />
      <meshStandardMaterial color="#d1d5db" />
    </mesh>
  );

  // Đường from sảnh sang gym
  paths.push(
    <mesh key="building-gym" rotation={[-Math.PI / 2, 0, 0]} position={[15, 0.02, -4]}>
      <planeGeometry args={[10, 6]} />
      <meshStandardMaterial color="#d1d5db" />
    </mesh>
  );

  return <group>{paths}</group>;
}

// ====== Full Building (original) ======
function Building({ highlightAmenities, onSelectFloor, selectedFloor }) {
  return (
    <group position={[0, 0, 0]}>
      <FloorSlabs onSelectFloor={onSelectFloor} selectedFloor={selectedFloor} />
      <Apartments />
      <CoreShaft />

      {/* Tầng trệt */}
      <GroundFloorRooms />

      {/* WC + Cầu thang mỗi tầng */}
      <AmenitiesPerFloor />

      {/* Các tiện ích */}
      <Pool highlight={highlightAmenities.pool} />
      <Park highlight={highlightAmenities.park} />
      <Gym highlight={highlightAmenities.gym} />
      <SecurityGate highlight={highlightAmenities.security} />

      {/* Đường đi */}
      <Pathways />
    </group>
  );
}

// ====== Reuse components (GroundFloorRooms, AmenitiesPerFloor) ======
function GroundFloorRooms() {
  const size = [4, 2.8, 4];
  const y = APT_H / 2;

  return (
    <group>
      <mesh position={[-10, y, -8]} castShadow>
        <boxGeometry args={size} />
        <meshStandardMaterial color="#fde68a" />
        <Text position={[0, 2, 0]} fontSize={0.6} color="#333">
          Quản trị
        </Text>
      </mesh>

      <mesh position={[0, y, -8]} castShadow>
        <boxGeometry args={size} />
        <meshStandardMaterial color="#fca5a5" />
        <Text position={[0, 2, 0]} fontSize={0.6} color="#333">
          Kế toán
        </Text>
      </mesh>

      <mesh position={[10, y, -8]} castShadow>
        <boxGeometry args={size} />
        <meshStandardMaterial color="#93c5fd" />
        <Text position={[0, 2, 0]} fontSize={0.6} color="#333">
          Giám đốc
        </Text>
      </mesh>
    </group>
  );
}

function AmenitiesPerFloor() {
  const rooms = [];
  const wcSize = [2, 2.6, 2];
  const stairSize = [2, 2.6, 2];

  for (let f = 0; f < FLOORS; f++) {
    const y = f * FLOOR_HEIGHT + APT_H / 2;

    rooms.push(
      <group key={f}>
        <mesh position={[GRID_X * (APT_W + GAP) / 2 + 2, y, 0]} castShadow>
          <boxGeometry args={wcSize} />
          <meshStandardMaterial color="#bbf7d0" />
          <Text position={[0, 2, 0]} fontSize={0.5} color="#222">
            WC
          </Text>
        </mesh>

        <mesh position={[-GRID_X * (APT_W + GAP) / 2 - 2, y, 0]} castShadow>
          <boxGeometry args={stairSize} />
          <meshStandardMaterial color="#fcd34d" />
          <Text position={[0, 2, 0]} fontSize={0.5} color="#222">
            Stair
          </Text>
        </mesh>
      </group>
    );
  }
  return <group>{rooms}</group>;
}

// ====== Camera controller: smoothly move camera to top-down view when floor selected ======

// ====== HUD & Legend ======
function HUD({ highlightAmenities, setHighlightAmenities, selectedFloor, onCloseFloor }) {
  const toggle = (key) => setHighlightAmenities((s) => ({ ...s, [key]: !s[key] }));
  return (
    <div
      style={{
        position: "absolute",
        top: 12,
        left: 12,
        background: "rgba(255,255,255,0.9)",
        padding: "10px",
        borderRadius: "10px",
        boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
        fontSize: "14px",
        zIndex: 30,
      }}
    >
      <h3 style={{ margin: "0 0 8px 0" }}>Mô phỏng Chung cư</h3>
      <div style={{ display: "flex", gap: 6, flexWrap: "wrap" }}>
        <button onClick={() => toggle("pool")}>Hồ bơi</button>
        <button onClick={() => toggle("park")}>Công viên</button>
        <button onClick={() => toggle("gym")}>Phòng gym</button>
        <button onClick={() => toggle("security")}>Bảo vệ</button>
        {selectedFloor !== null ? (
          <button onClick={onCloseFloor} style={{ background: "#ef4444", color: "white", marginLeft: 6 }}>
            Quay lại
          </button>
        ) : null}
      </div>
      <div style={{ marginTop: 6, fontSize: 12, color: "#444" }}>Nhấn vào 1 tầng để xem sơ đồ mặt bằng (top-down)</div>
    </div>
  );
}

function Legend() {
  return (
    <div
      style={{
        position: "absolute",
        bottom: 20,
        left: "50%",
        transform: "translateX(-50%)",
        background: "rgba(255,255,255,0.9)",
        padding: "8px 12px",
        borderRadius: 10,
        boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
        fontSize: 12,
        display: "flex",
        gap: 10,
        zIndex: 20,
      }}
    >
      <span><span style={{ background: "#9adcf7", display: "inline-block", width: 12, height: 12, marginRight: 6 }}></span>Hồ bơi</span>
      <span><span style={{ background: "#65a30d", display: "inline-block", width: 12, height: 12, marginRight: 6 }}></span>Công viên</span>
      <span><span style={{ background: "#e6c88f", display: "inline-block", width: 12, height: 12, marginRight: 6 }}></span>Gym</span>
      <span><span style={{ background: "#bbf7d0", display: "inline-block", width: 12, height: 12, marginRight: 6 }}></span>WC</span>
      <span><span style={{ background: "#fcd34d", display: "inline-block", width: 12, height: 12, marginRight: 6 }}></span>Cầu thang</span>
      <span><span style={{ background: "#d1d5db", display: "inline-block", width: 12, height: 12, marginRight: 6 }}></span>Đường đi</span>
    </div>
  );
}

// ====== MAIN EXPORT COMPONENT (full scene + interactions) ======
export default function ApartmentSimulator3D() {
  const [highlightAmenities, setHighlightAmenities] = useState({
    pool: true,
    park: true,
    gym: true,
    security: true
  });
  const [selectedFloor, setSelectedFloor] = useState(null);

  const onCloseFloor = () => setSelectedFloor(null);

  return (
    <div style={{ width: "100%", height: "90vh", position: "relative", background: "#f0f0f0" }}>
      {/* HUD + Legend */}
      <HUD
        highlightAmenities={highlightAmenities}
        setHighlightAmenities={setHighlightAmenities}
        selectedFloor={selectedFloor}
        onCloseFloor={onCloseFloor}
      />
      <Legend />

      {/* Nếu có chọn tầng thì overlay hiện */}
      {selectedFloor !== null && (
        <FloorPlanOverlay
          floor={selectedFloor}
          onClose={() => setSelectedFloor(null)}
        />
      )}

      {/* Scene 3D */}
      <Canvas shadows camera={{ position: [30, 30, 40], fov: 40 }} dpr={[1, 2]}>
        <SunLight />
        <Environment preset="city" />
        <Ground />

        <Building
          highlightAmenities={highlightAmenities}
          onSelectFloor={(f) => setSelectedFloor(f)}
          selectedFloor={selectedFloor}
        />

        <OrbitControls enableDamping dampingFactor={0.1} makeDefault target={[0, 10, 0]} />
        <StatsGl />
      </Canvas>
    </div>
  );
}

