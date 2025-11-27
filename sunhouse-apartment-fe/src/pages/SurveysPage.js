import React, { useEffect, useState, useCallback } from 'react';
import Sidebar from '../components/Sidebar';
import { authApis, endpoints } from '../configs/Apis';
import cookie from 'react-cookies';
import { useUser } from "../contexts/UserContext";

import { Modal, Button, Form } from 'react-bootstrap';

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

  // Lưu câu trả lời người dùng, dạng { questionId: answer }
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
  }, [keyword, user.id]); // dependency chuẩn

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

    // Validate: mọi câu hỏi đều phải có câu trả lời (backend của bạn yêu cầu như vậy)
    for (let q of questions) {
      const val = answers[q.id];
      if (q.type === 'MULTIPLE_CHOICE') {
        // với trắc nghiệm phải có optionId (số)
        if (val === undefined || val === null || val === '') {
          alert(`Bạn phải chọn đáp án cho câu: "${q.content}"`);
          return;
        }
      } else {
        // tự luận -> phải có text (không phải chỉ whitespace)
        if (typeof val !== 'string' || val.trim() === '') {
          alert(`Bạn phải nhập câu trả lời cho câu: "${q.content}"`);
          return;
        }
      }
    }

    // Build answers array theo đúng DTO AnswerRequest:
    // - MULTIPLE_CHOICE => { questionId, optionId }
    // - SHORT_ANSWER => { questionId, answer }
    const answersPayload = questions.map((q) => {
      const val = answers[q.id];
      if (q.type === 'MULTIPLE_CHOICE') {
        // đảm bảo gửi số
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
      // Gọi API: POST /survey/submit?userId=<user.id>
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
    <div className="d-flex flex-column" style={{ minHeight: '100vh', backgroundColor: '#c0dbed' }}>
      <Sidebar />
      <div className="content p-4" style={{ marginLeft: '250px', flex: 1 }}>
        {/* Search */}
        <div className="search-bar mb-4 d-flex align-items-center justify-content-between" style={{ gap: '10px' }}>
          <input
            type="text"
            className="form-control rounded-pill"
            placeholder="Tìm kiếm..."
            style={{ width: '250px' }}
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </div>

        {/* Danh sách khảo sát */}
        {loading ? (
          <p>Đang tải khảo sát...</p>
        ) : (
          <div className="card-container d-flex flex-wrap gap-3">
            {surveys.length > 0 ? (
              surveys.map((item) => (
                <div
                  key={item.id}
                  className="card"
                  style={{
                    width: '300px',
                    backgroundColor: '#fff',
                    borderRadius: '10px',
                    boxShadow: '0 2px 5px rgba(0,0,0,0.1)',
                    cursor: 'pointer'
                  }}
                  onClick={() => openSurveyModal(item)}
                >
                  <div className="card-body text-center p-3">
                    <h6 className="mb-2">{item.title}</h6>
                    <p className="text-muted mb-2" style={{ fontSize: '0.9rem' }}>
                      {new Date(item.createdAt).toLocaleDateString('vi-VN')}
                    </p>
                    <button
                      className="btn btn-primary w-100"
                      style={{ padding: '5px', fontSize: '0.9rem' }}
                      type="button"
                      disabled
                    >
                      {item.type === 'MULTIPLE_CHOICE' ? 'Khảo sát trắc nghiệm' : 'Khảo sát tự luận'}
                    </button>
                  </div>
                </div>
              ))
            ) : (
              <p>Không tìm thấy khảo sát nào.</p>
            )}
          </div>
        )}

        {/* Modal hiện câu hỏi khảo sát */}
        <Modal show={showModal} onHide={closeSurveyModal} size="lg" scrollable>
          <Modal.Header closeButton>
            <Modal.Title>{selectedSurvey ? selectedSurvey.title : "Khảo sát"}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {loadingQuestions && <p>Đang tải câu hỏi...</p>}
            {errorQuestions && <p style={{ color: 'red' }}>{errorQuestions}</p>}
            {!loadingQuestions && !errorQuestions && questions.length === 0 && (
              <p>Khảo sát chưa có câu hỏi.</p>
            )}

            {!loadingQuestions && !errorQuestions && questions.length > 0 && (
              <Form>
                {questions.map((q) => (
                  <Form.Group key={q.id} className="mb-3">
                    <Form.Label><b>{q.content}</b></Form.Label>
                    {q.type === 'MULTIPLE_CHOICE' ? (
                      q.options.map((opt) => (
                        <Form.Check
                          type="radio"
                          key={opt.id}
                          label={opt.content}
                          name={`question-${q.id}`}
                          id={`question-${q.id}-option-${opt.id}`}
                          value={opt.id}
                          checked={answers[q.id] === String(opt.id) || answers[q.id] === opt.id}
                          onChange={(e) => handleAnswerChange(q.id, e.target.value)}
                        />
                      ))
                    ) : (
                      <Form.Control
                        as="textarea"
                        rows={3}
                        value={answers[q.id] || ""}
                        onChange={(e) => handleAnswerChange(q.id, e.target.value)}
                        placeholder="Nhập câu trả lời..."
                      />
                    )}
                  </Form.Group>
                ))}
              </Form>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={closeSurveyModal}>
              Đóng
            </Button>
            <Button variant="primary" onClick={handleSubmit} disabled={loadingQuestions}>
              Gửi câu trả lời
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};

export default SurveysPage;
