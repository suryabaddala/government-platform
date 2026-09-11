import React, { useState } from "react";
import "./App.css";

function App() {
  // =========================
  // STATES
  // =========================

  const [search, setSearch] = useState("");
  const [showAI, setShowAI] = useState(false);
  const [showLogin, setShowLogin] = useState(false);
  const [authMode, setAuthMode] = useState("login");
  const [loginName, setLoginName] = useState("");
  const [loginEmail, setLoginEmail] = useState("");
  const [loginPassword, setLoginPassword] = useState("");
  const [loggedInUser, setLoggedInUser] = useState(null);

  const [selectedService, setSelectedService] = useState(null);
  const [showApplicationForm, setShowApplicationForm] = useState(false);

  const [applicationId, setApplicationId] = useState("");
  const [trackId, setTrackId] = useState("");
  const [trackedApplication, setTrackedApplication] = useState(null);
  const [trackedStatus, setTrackedStatus] = useState("PROCESSING");

  // =========================
  // SERVICES
  // =========================

  const services = [
    {
      icon: "🪪",
      title: "Certificates",
      description:
        "Apply for birth, income, caste and other important government certificates.",
      documents: [
        "Aadhaar / Identity Proof",
        "Address Proof",
        "Passport Size Photograph",
        "Supporting Documents",
      ],
    },

    {
      icon: "🏠",
      title: "Housing",
      description:
        "Find housing schemes and government assistance for eligible citizens.",
      documents: [
        "Aadhaar / Identity Proof",
        "Address Proof",
        "Income Certificate",
        "Bank Account Details",
      ],
    },

    {
      icon: "🎓",
      title: "Education",
      description:
        "Access scholarships, education assistance and student services.",
      documents: [
        "Student ID",
        "Aadhaar Card",
        "Previous Academic Certificate",
        "Bank Account Details",
      ],
    },

    {
      icon: "💼",
      title: "Employment",
      description:
        "Find employment opportunities, skill development and government schemes.",
      documents: [
        "Aadhaar Card",
        "Educational Certificates",
        "Resume",
        "Address Proof",
      ],
    },

    {
      icon: "🏥",
      title: "Health",
      description:
        "Access government health services, schemes and assistance programs.",
      documents: [
        "Aadhaar Card",
        "Address Proof",
        "Health Documents",
        "Income Certificate if required",
      ],
    },

    {
      icon: "🌾",
      title: "Agriculture",
      description:
        "Access farmer schemes, agricultural assistance and related services.",
      documents: [
        "Aadhaar Card",
        "Land Documents",
        "Bank Account Details",
        "Farmer Registration",
      ],
    },

    {
      icon: "🚗",
      title: "Transport",
      description:
        "Access vehicle registration, transport and related government services.",
      documents: [
        "Aadhaar / Identity Proof",
        "Address Proof",
        "Vehicle Documents",
        "Insurance Documents",
      ],
    },

    {
      icon: "🏛️",
      title: "Municipal Services",
      description:
        "Access local government and municipal services from one platform.",
      documents: [
        "Identity Proof",
        "Address Proof",
        "Application Form",
        "Supporting Documents",
      ],
    },
  ];

  // =========================
  // POPULAR SERVICES
  // =========================

  const popularServices = [
    "Birth Certificate",
    "Income Certificate",
    "Caste Certificate",
    "Scholarship",
    "Housing Scheme",
    "Farmer Services",
  ];

  // =========================
  // SEARCH
  // =========================

  const filteredServices = services.filter((service) =>
    service.title.toLowerCase().includes(search.toLowerCase())
  );

  // =========================
  // OPEN SERVICE DETAILS
  // =========================

  const openServiceDetails = (service) => {
    setSelectedService(service);
    setShowApplicationForm(false);
  };

  // =========================
  // OPEN APPLICATION FORM
  // =========================

  const openApplicationForm = () => {
    setShowApplicationForm(true);
  };

  // =========================
  // CLOSE MODALS
  // =========================

  const closeServiceDetails = () => {
    setSelectedService(null);
    setShowApplicationForm(false);
  };

  // =========================
  // SUBMIT APPLICATION
  // =========================

  const submitApplication = async (event) => {
    event.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/applications", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ serviceName: selectedService.title }),
      });

      if (!response.ok) {
        throw new Error("Application submission failed");
      }

      const application = await response.json();
      const newId = application.applicationId;

      setApplicationId(newId);
      setTrackId(newId);
      setTrackedApplication(newId);
      setTrackedStatus(application.status);
      setShowApplicationForm(false);
      setSelectedService(null);

      alert(
        "Application submitted successfully!\n\nYour Application ID is:\n" +
          newId
      );
    } catch (error) {
      alert("Could not submit the application. Make sure the backend is running.");
    }
  };

  const trackApplication = async () => {
    const enteredId = trackId.trim().toUpperCase();

    if (enteredId === "") {
      alert("Please enter your Application ID.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/applications/${enteredId}`
      );

      if (!response.ok) {
        throw new Error("Application not found");
      }

      const application = await response.json();
      setApplicationId(application.applicationId);
      setTrackedApplication(application.applicationId);
      setTrackedStatus(application.status);
    } catch (error) {
      setTrackedApplication("NOT_FOUND");
    }
  };

  const login = async (event) => {
    event.preventDefault();
    try {
      const endpoint = authMode === "register" ? "register" : "login";
      const response = await fetch(`http://localhost:8080/api/auth/${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: loginName,
          email: loginEmail,
          password: loginPassword,
        }),
      });
      if (!response.ok) throw new Error("Authentication failed");
      const user = await response.json();
      setLoggedInUser(user);
      setShowLogin(false);
      setLoginName("");
      setLoginEmail("");
      setLoginPassword("");
    } catch (error) {
      alert(
        authMode === "register"
          ? "Registration failed. Use a new email and a password of at least 6 characters."
          : "Login failed. Check your email and password."
      );
    }
  };

  // =========================
  // SCROLL FUNCTION
  // =========================

  const scrollToSection = (id) => {
    const element = document.getElementById(id);

    if (element) {
      element.scrollIntoView({
        behavior: "smooth",
      });
    }
  };

  // =========================
  // RETURN
  // =========================

  return (
    <div className="app">

      {/* =========================
          NAVBAR
      ========================= */}

      <header className="navbar">

        <div className="logo-area">

          <div className="logo-icon">
            G
          </div>

          <div>
            <h2>GovOne</h2>
            <span>Government Services</span>
          </div>

        </div>


        <nav className="nav-links">

          <a href="#home">
            Home
          </a>

          <a href="#services">
            Services
          </a>

          <a href="#track">
            Track Application
          </a>

          <button
            className="ai-nav-button"
            onClick={() => setShowAI(true)}
          >
            🤖 AI Assistant
          </button>

        </nav>


        <button className="login-button" onClick={() => setShowLogin(true)}>
          {loggedInUser ? `Hi, ${loggedInUser.name}` : "Login"}
        </button>

      </header>


      {/* =========================
          HERO
      ========================= */}

      <main>

        <section
          className="hero"
          id="home"
        >

          <div className="hero-content">

            <div className="welcome-badge">
              🇮🇳 CITIZEN SERVICE PLATFORM
            </div>


            <h1>
              Government Services,
              <br />
              <span>Made Simple.</span>
            </h1>


            <p>
              Find government services, apply online and
              track your applications — all from one place.
            </p>


            {/* MAIN SEARCH */}

            <div className="main-search">

              <span>
                🔍
              </span>

              <input
                type="text"
                placeholder="What service are you looking for?"
                value={search}
                onChange={(event) =>
                  setSearch(event.target.value)
                }
              />

              <button
                onClick={() =>
                  scrollToSection("services")
                }
              >
                Search
              </button>

            </div>


            {/* QUICK ACTIONS */}

            <div className="quick-actions">

              <button
                onClick={() =>
                  scrollToSection("services")
                }
              >
                🏛️ Browse Services
              </button>


              <button
                onClick={() =>
                  scrollToSection("track")
                }
              >
                📋 Track Application
              </button>

            </div>

          </div>


          {/* AI CARD */}

          <div className="hero-card">

            <div className="ai-circle">
              🤖
            </div>

            <h3>
              Need Help?
            </h3>

            <p>
              Our AI Assistant can help you find
              the right government service.
            </p>

            <button
              onClick={() => setShowAI(true)}
            >
              Ask GovOne AI →
            </button>

          </div>

        </section>


        {/* =========================
            STATS
        ========================= */}

        <section className="stats">

          <div className="stat-item">
            <strong>50+</strong>
            <span>Government Services</span>
          </div>

          <div className="stat-item">
            <strong>6+</strong>
            <span>Departments</span>
          </div>

          <div className="stat-item">
            <strong>24/7</strong>
            <span>Digital Access</span>
          </div>

          <div className="stat-item">
            <strong>AI</strong>
            <span>Smart Assistance</span>
          </div>

        </section>


        {/* =========================
            SERVICES
        ========================= */}

        <section
          className="services-section"
          id="services"
        >

          <div className="section-title">

            <span>
              GOVERNMENT SERVICES
            </span>

            <h2>
              What do you need help with?
            </h2>

            <p>
              Choose a service category to get started.
            </p>

          </div>


          {/* SERVICE SEARCH */}

          <div className="service-search">

            <span>
              🔍
            </span>

            <input
              type="text"
              placeholder="Search services..."
              value={search}
              onChange={(event) =>
                setSearch(event.target.value)
              }
            />

          </div>


          {/* SERVICE CARDS */}

          <div className="service-grid">

            {filteredServices.map(
              (service, index) => (

                <div
                  className="service-card"
                  key={index}
                >

                  <div className="service-icon">
                    {service.icon}
                  </div>


                  <h3>
                    {service.title}
                  </h3>


                  <p>
                    {service.description}
                  </p>


                  <button
                    onClick={() =>
                      openServiceDetails(service)
                    }
                  >
                    View Services →
                  </button>

                </div>

              )
            )}

          </div>


          {/* NO SEARCH RESULTS */}

          {filteredServices.length === 0 && (

            <div className="no-results">

              <span>
                🔍
              </span>

              <h3>
                No service found
              </h3>

              <p>
                Try searching with another keyword.
              </p>

            </div>

          )}

        </section>


        {/* =========================
            POPULAR SERVICES
        ========================= */}

        <section className="popular-section">

          <div className="section-title">

            <span>
              POPULAR SERVICES
            </span>

            <h2>
              Frequently Used Services
            </h2>

          </div>


          <div className="popular-grid">

            {popularServices.map(
              (service, index) => (

                <button
                  className="popular-card"
                  key={index}
                  onClick={() => {
                    setSearch(service);
                    scrollToSection("services");
                  }}
                >

                  <span>
                    📄
                  </span>

                  <div>

                    <strong>
                      {service}
                    </strong>

                    <small>
                      Search service →
                    </small>

                  </div>

                </button>

              )
            )}

          </div>

        </section>


        {/* =========================
            APPLICATION TRACKING
        ========================= */}

        <section
          className="tracking-section"
          id="track"
        >

          <div className="tracking-content">

            <span>
              APPLICATION TRACKING
            </span>

            <h2>
              Track Your Application
            </h2>

            <p>
              Enter your Application ID to see
              the latest status of your application.
            </p>


            <div className="tracking-box">

              <input
                type="text"
                placeholder="Example: GOV389043"
                value={trackId}
                onChange={(event) =>
                  setTrackId(event.target.value)
                }
                onKeyDown={(event) => {
                  if (event.key === "Enter") {
                    trackApplication();
                  }
                }}
              />


              <button
                onClick={trackApplication}
              >
                Track Status
              </button>

            </div>


            <small>
              Example Application ID: GOV389043
            </small>

          </div>


          {/* STATUS CARD */}

          <div className="status-preview">

            <div className="status-header">

              <div>

                <small>
                  APPLICATION ID
                </small>

                <strong>
                  {applicationId || "GOV389043"}
                </strong>

              </div>


                <span className="status-badge">
                {trackedStatus}
              </span>

            </div>


            <div className="progress-line">

              <div className="progress-active"></div>

            </div>


            <div className="status-steps">

              <div className="status-step active">

                <span>
                  ✓
                </span>

                <small>
                  Submitted
                </small>

              </div>


              <div className="status-step active">

                <span>
                  ✓
                </span>

                <small>
                  Verified
                </small>

              </div>


              <div className="status-step current">

                <span>
                  ●
                </span>

                <small>
                  Processing
                </small>

              </div>


              <div className="status-step">

                <span>
                  ○
                </span>

                <small>
                  Approved
                </small>

              </div>

            </div>

          </div>

        </section>


        {/* =========================
            TRACK RESULT
        ========================= */}

        {trackedApplication === "NOT_FOUND" && (

          <div className="track-result-error">

            ❌ Application ID not found.
            Please check the ID and try again.

          </div>

        )}


        {trackedApplication &&
          trackedApplication !== "NOT_FOUND" && (

            <section className="track-result">

              <h2>
                Application Status
              </h2>

              <p>
                Application ID:
                <strong>
                  {" "}{trackedApplication}
                </strong>
              </p>

              <div className="result-timeline">

                <div className="result-step completed">

                  <span>
                    ✓
                  </span>

                  <div>
                    <strong>
                      Application Submitted
                    </strong>

                    <small>
                      Completed
                    </small>
                  </div>

                </div>


                <div className="result-step completed">

                  <span>
                    ✓
                  </span>

                  <div>
                    <strong>
                      Documents Verified
                    </strong>

                    <small>
                      Completed
                    </small>
                  </div>

                </div>


                <div className="result-step current">

                  <span>
                    ●
                  </span>

                  <div>
                    <strong>
                      {trackedStatus}
                    </strong>

                    <small>
                      {trackedStatus}
                    </small>
                  </div>

                </div>


                <div className="result-step">

                  <span>
                    ○
                  </span>

                  <div>
                    <strong>
                      Application Approved
                    </strong>

                    <small>
                      Pending
                    </small>
                  </div>

                </div>

              </div>

            </section>

          )}


        {/* =========================
            WHY GOVONE
        ========================= */}

        <section className="why-section">

          <div className="section-title">

            <span>
              WHY GOVONE?
            </span>

            <h2>
              Designed for Citizens
            </h2>

            <p>
              Everything you need to access government
              services easily and securely.
            </p>

          </div>


          <div className="why-grid">

            <div className="why-card">

              <div>
                🔎
              </div>

              <h3>
                Easy to Find
              </h3>

              <p>
                Search and discover services quickly.
              </p>

            </div>


            <div className="why-card">

              <div>
                📝
              </div>

              <h3>
                Simple Application
              </h3>

              <p>
                Apply for services through easy steps.
              </p>

            </div>


            <div className="why-card">

              <div>
                📋
              </div>

              <h3>
                Track Easily
              </h3>

              <p>
                Know your application status anytime.
              </p>

            </div>


            <div className="why-card">

              <div>
                🤖
              </div>

              <h3>
                AI Assistance
              </h3>

              <p>
                Get intelligent guidance whenever you need it.
              </p>

            </div>

          </div>

        </section>


        {/* =========================
            AI SECTION
        ========================= */}

        <section className="ai-section">

          <div>

            <span>
              SMART ASSISTANCE
            </span>

            <h2>
              Not sure which service you need?
            </h2>

            <p>
              Ask GovOne AI. It can guide you to the
              right government service and explain
              the application process.
            </p>

            <button
              onClick={() => setShowAI(true)}
            >
              🤖 Talk to GovOne AI
            </button>

          </div>


          <div className="ai-large-icon">
            🤖
          </div>

        </section>

      </main>


      {/* =========================
          FOOTER
      ========================= */}

      <footer className="footer">

        <div className="footer-brand">

          <h2>
            GovOne
          </h2>

          <p>
            One platform for simpler and smarter
            government services.
          </p>

        </div>


        <div>

          <h4>
            Platform
          </h4>

          <a href="#home">
            Home
          </a>

          <a href="#services">
            Services
          </a>

          <a href="#track">
            Track Application
          </a>

        </div>


        <div>

          <h4>
            Support
          </h4>

          <a href="#home">
            Help Center
          </a>

          <a href="#home">
            FAQs
          </a>

          <a href="#home">
            Contact Us
          </a>

        </div>

      </footer>


      {/* =========================
          SERVICE DETAILS MODAL
      ========================= */}

      {selectedService && !showApplicationForm && (

        <div className="modal-background">

          <div className="service-details-modal">

            <button
              className="modal-close"
              onClick={closeServiceDetails}
            >
              ✕
            </button>


            <div className="details-icon">
              {selectedService.icon}
            </div>


            <h2>
              {selectedService.title}
            </h2>


            <p className="details-description">
              {selectedService.description}
            </p>


            <div className="details-section">

              <h3>
                Who can apply?
              </h3>

              <p>
                Citizens who meet the eligibility
                requirements of this service can
                apply through the GovOne platform.
              </p>

            </div>


            <div className="details-section">

              <h3>
                Required Documents
              </h3>

              <ul>

                {selectedService.documents.map(
                  (document, index) => (

                    <li key={index}>
                      ✓ {document}
                    </li>

                  )
                )}

              </ul>

            </div>


            <div className="details-info">

              <div>

                <strong>
                  Processing Time
                </strong>

                <span>
                  7–15 Working Days
                </span>

              </div>


              <div>

                <strong>
                  Application Mode
                </strong>

                <span>
                  Online
                </span>

              </div>

            </div>


            <button
              className="details-apply-button"
              onClick={openApplicationForm}
            >
              Apply Now →
            </button>

          </div>

        </div>

      )}


      {/* =========================
          APPLICATION FORM
      ========================= */}

      {selectedService && showApplicationForm && (

        <div className="modal-background">

          <div className="application-form">

            <button
              className="modal-close"
              onClick={closeServiceDetails}
            >
              ✕
            </button>


            <h2>
              Apply for Service
            </h2>


            <p className="form-service">
              {selectedService.icon}{" "}
              {selectedService.title}
            </p>


            <form onSubmit={submitApplication}>

              <label>
                Applicant Name
              </label>

              <input
                type="text"
                placeholder="Enter your full name"
                required
              />


              <label>
                Mobile Number
              </label>

              <input
                type="tel"
                placeholder="Enter mobile number"
                required
              />


              <label>
                Email Address
              </label>

              <input
                type="email"
                placeholder="Enter email address"
                required
              />


              <label>
                Address
              </label>

              <textarea
                rows="3"
                placeholder="Enter your address"
                required
              />


              <label>
                Upload Document
              </label>

              <input
                type="file"
                />

                <button
                type="submit"
                className="submit-btn"
                >
                  Submit Application
                  </button>
                  </form>
                  </div>
                  </div>
      )}
      {showLogin && (
        <div className="modal-background">
          <div className="ai-modal">
            <button
              className="modal-close"
              onClick={() => setShowLogin(false)}
            >
              ✕
            </button>

            <h2>{authMode === "login" ? "Welcome back" : "Create your account"}</h2>

            <form onSubmit={login}>
              {authMode === "register" && (
                <input
                  type="text"
                  placeholder="Full name"
                  value={loginName}
                  onChange={(event) => setLoginName(event.target.value)}
                  required
                />
              )}
              <input
                type="email"
                placeholder="Email address"
                value={loginEmail}
                onChange={(event) => setLoginEmail(event.target.value)}
                required
              />
              <input
                type="password"
                placeholder="Password"
                value={loginPassword}
                onChange={(event) => setLoginPassword(event.target.value)}
                minLength="6"
                required
              />
              <button type="submit" className="submit-btn">
                {authMode === "login" ? "Login" : "Register"}
              </button>
            </form>

            <button
              onClick={() => setAuthMode(authMode === "login" ? "register" : "login")}
            >
              {authMode === "login"
                ? "Create a new account"
                : "Already have an account? Login"}
            </button>
          </div>
        </div>
      )}

      {/* =========================
          AI ASSISTANT MODAL
      ========================= */}

      {showAI && (

        <div className="modal-background">

          <div className="ai-modal">

            <button
              className="modal-close"
              onClick={() => setShowAI(false)}
            >
              ✕
            </button>


            <div className="ai-modal-icon">
              🤖
            </div>


            <h2>
              GovOne AI Assistant
            </h2>


            <p>
              Hello! 👋 How can I help you today?
            </p>


            <div className="ai-options">

              <button
                onClick={() => {
                  setSearch("Certificates");
                  setShowAI(false);
                  scrollToSection("services");
                }}
              >
                🪪 I need a certificate
              </button>


              <button
                onClick={() => {
                  setSearch("Housing");
                  setShowAI(false);
                  scrollToSection("services");
                }}
              >
                🏠 I need housing assistance
              </button>


              <button
                onClick={() => {
                  setSearch("Education");
                  setShowAI(false);
                  scrollToSection("services");
                }}
              >
                🎓 I want education services
              </button>


              <button
                onClick={() => {
                  setShowAI(false);
                  scrollToSection("track");
                }}
              >
                📋 I want to track my application
              </button>

            </div>


            <div className="ai-input">

              <input
                type="text"
                placeholder="Ask about a government service..."
              />

              <button>
                ➤
              </button>

            </div>

          </div>

        </div>

      )}

    </div>
  );
}

export default App;

