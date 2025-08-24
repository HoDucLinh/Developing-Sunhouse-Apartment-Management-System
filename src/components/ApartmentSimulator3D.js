import React, { useMemo, useRef, useState, useEffect } from "react";
import * as THREE from "three";
import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls, Environment, Text, StatsGl, Float } from "@react-three/drei";

// ====== CONSTANTS ======
const FLOORS = 10;
const APTS_PER_FLOOR = 10;
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

function FloorSlabs() {
  const slabs = [];
  const totalWidth = GRID_X * (APT_W + GAP) - GAP + 0.6;
  const totalDepth = GRID_Z * (APT_D + GAP) - GAP + 0.6;
  for (let f = 0; f <= FLOORS; f++) {
    slabs.push(
      <mesh key={f} position={[0, f * FLOOR_HEIGHT, 0]} receiveShadow castShadow>
        <boxGeometry args={[totalWidth, 0.2, totalDepth]} />
        <meshStandardMaterial color="#eeeeee" />
      </mesh>
    );
  }
  return <group>{slabs}</group>;
}

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
      <Text position={[0, 0.8, size[1] / 2 + 1.2]} fontSize={0.8} color={highlight ? "#0077cc" : "#333"}>Hồ bơi</Text>
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
      <Text position={[0, size[1] + 0.2, 0]} fontSize={0.8} color="#333">Phòng gym</Text>
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
      <Text position={[0, size[1] + 0.2, 0]} fontSize={0.7} color="#333">Khu bảo vệ</Text>
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
      <Text position={[0, 0.4, depth / 2 + 1]} fontSize={0.7} color="#1f2937">Công viên</Text>
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

function Building({ highlightAmenities }) {
  return (
    <group position={[0, 0, 0]}>
      <FloorSlabs />
      <Apartments />
      <CoreShaft />
      <Pool highlight={highlightAmenities.pool} />
      <Park highlight={highlightAmenities.park} />
      <Gym highlight={highlightAmenities.gym} />
      <SecurityGate highlight={highlightAmenities.security} />
    </group>
  );
}

function HUD({ highlightAmenities, setHighlightAmenities }) {
  const toggle = (key) => setHighlightAmenities((s) => ({ ...s, [key]: !s[key] }));
  return (
    <div style={{
      position: "absolute",
      top: 20,
      left: 20,
      background: "rgba(255,255,255,0.8)",
      padding: "10px",
      borderRadius: "10px",
      boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
      fontSize: "14px"
    }}>
      <h2 style={{ margin: "0 0 8px" }}>Mô phỏng Chung cư</h2>
      <div style={{ display: "flex", flexWrap: "wrap", gap: "6px" }}>
        <button onClick={() => toggle("pool")}>Hồ bơi</button>
        <button onClick={() => toggle("park")}>Công viên</button>
        <button onClick={() => toggle("gym")}>Phòng gym</button>
        <button onClick={() => toggle("security")}>Khu bảo vệ</button>
      </div>
      <div style={{ marginTop: "6px", fontSize: "12px", color: "#555" }}>
        Giữ chuột phải để xoay • Lăn để zoom • Kéo để pan
      </div>
    </div>
  );
}

function Legend() {
  return (
    <div style={{
      position: "absolute",
      bottom: 20,
      left: "50%",
      transform: "translateX(-50%)",
      background: "rgba(255,255,255,0.8)",
      padding: "8px 12px",
      borderRadius: "10px",
      boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
      fontSize: "13px",
      display: "flex",
      gap: "12px"
    }}>
      <span><span style={{ background: "#9adcf7", display: "inline-block", width: 12, height: 12, marginRight: 4 }}></span> Hồ bơi</span>
      <span><span style={{ background: "#65a30d", display: "inline-block", width: 12, height: 12, marginRight: 4 }}></span> Công viên</span>
      <span><span style={{ background: "#e6c88f", display: "inline-block", width: 12, height: 12, marginRight: 4 }}></span> Gym</span>
      <span><span style={{ background: "#c4c4c4", display: "inline-block", width: 12, height: 12, marginRight: 4 }}></span> Bảo vệ</span>
    </div>
  );
}

export default function ApartmentSimulator3D() {
  const [highlightAmenities, setHighlightAmenities] = useState({ pool: true, park: true, gym: true, security: true });

  return (
    <div style={{ width: "100%", height: "80vh", position: "relative", background: "#f0f0f0" }}>
      <HUD highlightAmenities={highlightAmenities} setHighlightAmenities={setHighlightAmenities} />
      <Legend />
      <Canvas shadows camera={{ position: [30, 30, 40], fov: 40 }} dpr={[1, 2]}>
        <SunLight />
        <Environment preset="city" />
        <Ground />
        <Building highlightAmenities={highlightAmenities} />
        <OrbitControls enableDamping dampingFactor={0.1} makeDefault target={[0, 10, 0]} />
        <StatsGl />
      </Canvas>
    </div>
  );
}
