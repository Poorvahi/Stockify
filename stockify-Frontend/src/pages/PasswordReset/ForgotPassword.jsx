import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Input from "../../components/Input/Input";
import { toast } from "react-toastify";
import { forgotPassword, verifyOtp, resetPassword } from "../../services/ApiService";
import "../Login/Login.css"; // Reuse login page styles

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [step, setStep] = useState(1);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (step === 1) {
        await forgotPassword({ email });
        toast.success("OTP sent to your email.");
        setStep(2);
        return;
      }
      if (step === 2) {
        await verifyOtp({ email, otp });
        toast.success("OTP verified.");
        setStep(3);
        return;
      }
      await resetPassword({ email, newPassword });
      toast.success("Password reset successful.");
      setTimeout(() => navigate("/login"), 1000);
    } catch (err) {
      toast.error(err?.response?.data?.message || "Unable to process request");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <i className="fa-solid fa-warehouse"></i>
          <h1 className="app-title">Stockify</h1>
          <p className="app-subtitle">Reset your password</p>
        </div>
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label className="form-label">{step === 1 ? "Registered Email" : "Email"}</label>
            <Input name="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Enter registered email" className="form-input" required />
          </div>
          {step >= 2 && (
            <div className="form-group">
              <label className="form-label">OTP</label>
              <Input name="otp" type="text" value={otp} onChange={(e) => setOtp(e.target.value)} placeholder="Enter 6-digit OTP" className="form-input" required />
            </div>
          )}
          {step === 3 && (
            <div className="form-group">
              <label className="form-label">New Password</label>
              <Input name="newPassword" type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} placeholder="Enter new password" className="form-input" required />
            </div>
          )}
          <div style={{ marginBottom: "1rem" }}>
            <Link to="/login" className="register-link">Back to Login</Link>
          </div>
          <button type="submit" className="login-button">
            {step === 1 ? "Send OTP" : step === 2 ? "Verify OTP" : "Reset Password"}
          </button>
        </form>
      </div>
    </div>
  );
}