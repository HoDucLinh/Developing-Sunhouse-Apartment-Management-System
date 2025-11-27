
import ImageSlider from "../components/carousel";
import HeaderHome from "../components/HeaderHome";

const Home = () => {

  return (
    <>
        <HeaderHome></HeaderHome>
        <div style={{ marginTop: "80px" }}>
            <ImageSlider />
        </div>
    </>
  );
};

export default Home;
