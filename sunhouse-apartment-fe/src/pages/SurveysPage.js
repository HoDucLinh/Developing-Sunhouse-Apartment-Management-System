import React, { useEffect, useState, useCallback } from 'react';
import Sidebar from '../components/Sidebar';
import { Container, Row, Col, Card, Button, Spinner, Modal, Form, Badge, Alert } from 'react-bootstrap';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { useUser } from "../contexts/UserContext";

const SurveysPage = () => {
  const { user } = useUser();
  const [surveys, setSurveys] = useState([]);
  const [loading, setLoading] = useState(true);
  const [keyword, setKeyword] = useState("");

  const [showModal, setShowModal] = useState(false);
  const [selectedSurvey, setSelectedSurvey] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [loadingQuestions, setLoadingQuestions] = useState(false);
  const [errorQuestions, setErrorQuestions] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const [answers, setAnswers] = useState({});

  const loadSurveys = useCallback(async () => {
    try {
      let token = cookie.load("token");
      let res = await authApis(token).get(endpoints.getSurveys, {
        params: { title: keyword, userId: user.id }
      });
      setSurveys(res.data);
    } catch (err) {
      console.error("Lỗi khi tải surveys:", err);
    } finally {
      setLoading(false);
    }
  }, [keyword, user.id]);

  useEffect(() => {
    loadSurveys();
  }, [loadSurveys]);

  const openSurveyModal = async (survey) => {
    setSelectedSurvey(survey);
    setShowModal(true);
    setLoadingQuestions(true);
    setErrorQuestions(null);
    setAnswers({});

    try {
      let token = cookie.load("token");
      let res = await authApis(token).get(endpoints.getQuestions(survey.id));
      setQuestions(res.data);
    } catch (err) {
      console.error("Lỗi khi lấy câu hỏi:", err);
      setErrorQuestions("Lỗi khi lấy câu hỏi khảo sát");
      setQuestions([]);
    } finally {
      setLoadingQuestions(false);
    }
  };

  const closeSurveyModal = () => {
    setShowModal(false);
    setSelectedSurvey(null);
    setQuestions([]);
    setAnswers({});
  };

  const handleAnswerChange = (questionId, value) => {
    setAnswers((prev) => ({
      ...prev,
      [questionId]: value,
    }));
  };

  const handleSubmit = async () => {
    if (!selectedSurvey) return;

    if (!user || !user.id) {
      alert('Bạn cần đăng nhập trước khi gửi khảo sát.');
      return;
    }

    for (let q of questions) {
      const val = answers[q.id];
      if (q.type === 'MULTIPLE_CHOICE') {
        if (val === undefined || val === null || val === '') {
          alert(`Bạn phải chọn đáp án cho câu: "${q.content}"`);
          return;
        }
      } else {
        if (typeof val !== 'string' || val.trim() === '') {
          alert(`Bạn phải nhập câu trả lời cho câu: "${q.content}"`);
          return;
        }
      }
    }

    const answersPayload = questions.map((q) => {
      const val = answers[q.id];
      if (q.type === 'MULTIPLE_CHOICE') {
        return {
          questionId: q.id,
          optionId: Number(val),
        };
      } else {
        return {
          questionId: q.id,
          answer: (val || '').toString(),
        };
      }
    });

    const payload = {
      surveyId: selectedSurvey.id,
      answers: answersPayload,
    };

    setSubmitting(true);
    try {
      const token = cookie.load('token');
      const url = `${endpoints.submitSurvey}?userId=${user.id}`;
      const res = await authApis(token).post(url, payload);

      alert('Gửi khảo sát thành công!');
      closeSurveyModal();
      await loadSurveys();
    } catch (err) {
      console.error('Lỗi khi gửi khảo sát:', err);
      const serverMsg = err?.response?.data;
      alert(serverMsg || 'Có lỗi xảy ra khi gửi khảo sát.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f8f9fa' }}>
      <Sidebar />

      <Container fluid className="px-5 py-4" style={{ marginLeft: '250px' }}>
        
        {/* Header */}
        <div className="d-flex justify-content-between align-items-center mb-5">
          <div>
            <h1 className="display-6 fw-bold text-dark">Khảo sát cư dân</h1>
            <p className="text-muted mb-0">Tham gia các cuộc khảo sát của ban quản lý chung cư</p>
          </div>
        </div>

        {/* Search Bar */}
        <div className="mb-5" style={{ maxWidth: '500px' }}>
          <Form.Control
            type="text"
            placeholder="Tìm kiếm khảo sát theo tiêu đề..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            size="lg"
            className="shadow-sm rounded-pill"
          />
        </div>

        {/* Danh sách khảo sát */}
        {loading ? (
          <div className="text-center py-5">
            <Spinner animation="border" variant="success" size="lg" />
            <p className="mt-3 text-muted">Đang tải danh sách khảo sát...</p>
          </div>
        ) : surveys.length === 0 ? (
          <Alert variant="info" className="text-center py-4">
            Không tìm thấy khảo sát nào phù hợp.
          </Alert>
        ) : (
          <Row className="g-4">
            {surveys.map((item) => (
              <Col key={item.id} xs={12} sm={6} md={4} lg={3}>
                <Card 
                  className="h-100 shadow border-0 rounded-4 overflow-hidden hover-card cursor-pointer"
                  onClick={() => openSurveyModal(item)}
                >
                  <Card.Body className="d-flex flex-column p-4">
                    <div className="mb-3">
                      <Badge bg="primary" pill className="mb-2">
                        {item.type === 'MULTIPLE_CHOICE' ? 'Trắc nghiệm' : 'Tự luận'}
                      </Badge>
                    </div>

                    <Card.Title className="fw-bold fs-5 mb-3">
                      {item.title}
                    </Card.Title>

                    <Card.Text className="text-muted small mb-4">
                      Ngày tạo: {new Date(item.createdAt).toLocaleDateString('vi-VN')}
                    </Card.Text>

                    <div className="mt-auto">
                      <Button 
                        variant="success" 
                        className="w-100 rounded-3 py-2 fw-medium"
                        onClick={(e) => {
                          e.stopPropagation();
                          openSurveyModal(item);
                        }}
                      >
                        Tham gia khảo sát
                      </Button>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        )}

        {/* Modal Khảo sát */}
        <Modal 
          show={showModal} 
          onHide={closeSurveyModal} 
          size="lg" 
          scrollable 
          centered
        >
          <Modal.Header closeButton className="border-0 pb-2">
            <Modal.Title className="fw-bold">
              {selectedSurvey?.title}
            </Modal.Title>
          </Modal.Header>

          <Modal.Body className="pt-2">
            {loadingQuestions && (
              <div className="text-center py-4">
                <Spinner animation="border" variant="success" />
                <p className="mt-2">Đang tải câu hỏi...</p>
              </div>
            )}

            {errorQuestions && <Alert variant="danger">{errorQuestions}</Alert>}

            {!loadingQuestions && !errorQuestions && questions.length === 0 && (
              <Alert variant="info">Khảo sát này chưa có câu hỏi nào.</Alert>
            )}

            {!loadingQuestions && !errorQuestions && questions.length > 0 && (
              <Form>
                {questions.map((q, index) => (
                  <div key={q.id} className="mb-5">
                    <h6 className="fw-semibold mb-3">
                      Câu {index + 1}: {q.content}
                    </h6>

                    {q.type === 'MULTIPLE_CHOICE' ? (
                      <div className="ms-2">
                        {q.options.map((opt) => (
                          <Form.Check
                            key={opt.id}
                            type="radio"
                            id={`q-${q.id}-opt-${opt.id}`}
                            label={opt.content}
                            name={`question-${q.id}`}
                            value={opt.id}
                            checked={answers[q.id] === String(opt.id) || answers[q.id] === opt.id}
                            onChange={(e) => handleAnswerChange(q.id, e.target.value)}
                            className="mb-2"
                          />
                        ))}
                      </div>
                    ) : (
                      <Form.Control
                        as="textarea"
                        rows={4}
                        value={answers[q.id] || ""}
                        onChange={(e) => handleAnswerChange(q.id, e.target.value)}
                        placeholder="Nhập câu trả lời của bạn..."
                        className="rounded-3"
                      />
                    )}
                  </div>
                ))}
              </Form>
            )}
          </Modal.Body>

          <Modal.Footer className="border-0 pt-2">
            <Button variant="secondary" onClick={closeSurveyModal}>
              Đóng
            </Button>
            <Button 
              variant="success" 
              onClick={handleSubmit} 
              disabled={submitting || loadingQuestions}
              className="px-4"
            >
              {submitting ? (
                <>
                  <Spinner animation="border" size="sm" className="me-2" />
                  Đang gửi...
                </>
              ) : "Gửi câu trả lời"}
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </div>
  );
};

export default SurveysPage;