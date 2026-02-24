import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useEffect, useState } from 'react';
import { endpoints, publicApi } from '../configs/Apis';

const ImageSlider = () => {
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    const fetchImages = async () => {
      try {
        const res = await publicApi.get(endpoints.getApartmentInfo(1)); // Giả sử apartmentId là 1
        if(res.data && res.data.images) {
          setImages(res.data.images);
        }
      } catch (error) {
        console.error('Error fetching images:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchImages();
  }, []);

  if (loading) {
    return <div className="text-center py-5">Loading images...</div>;
  }

  if (!images || images.length === 0) {
    return (
      <div className="text-center py-5">
        No images available
      </div>
    );
  }
  return (
    <Carousel>
      {images.map((img, index) => (
        <Carousel.Item key={img.id || index}>
          <img
            style={{ height: "550px", objectFit: "cover" }}
            className="d-block w-100"
            src={img.imageUrl}
            alt={`Slide ${index + 1}`}
          />
        </Carousel.Item>
      ))}
    </Carousel>
  );
}

export default ImageSlider;