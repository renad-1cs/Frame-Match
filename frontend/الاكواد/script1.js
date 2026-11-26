import {
    FaceLandmarker,
    FilesetResolver
} from "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision/vision_bundle.mjs";

// ------------------------------------------------------------
// API
// ------------------------------------------------------------
const API_URL = "http://localhost:8082/api";

const faceShapeIds = {
    Oval: 1,
    Round: 2,
    Square: 3,
    Heart: 4
};

// ------------------------------------------------------------
// Page elements
// ------------------------------------------------------------
const sections = document.querySelectorAll(".app-section");
const navLinks = document.querySelectorAll(".nav-link");

const camera = document.getElementById("camera");
const canvas = document.getElementById("faceCanvas");
const ctx = canvas.getContext("2d");

const cameraButton = document.getElementById("cameraButton");
const stopCameraButton = document.getElementById("stopCameraButton");
const cameraStatus = document.getElementById("cameraStatus");
const cameraPlaceholder = document.getElementById("cameraPlaceholder");
const viewResultsButton = document.getElementById("viewResultsButton");

const faceLengthElement = document.getElementById("faceLength");
const foreheadWidthElement = document.getElementById("foreheadWidth");
const cheekboneWidthElement = document.getElementById("cheekboneWidth");
const jawWidthElement = document.getElementById("jawWidth");
const faceShapeElement = document.getElementById("faceShape");
const faceShapeDescription = document.getElementById("faceShapeDescription");
const recommendationIntro = document.getElementById("recommendationIntro");
const viewRecommendationsButton = document.getElementById("viewRecommendationsButton");

const selectedGlassesName = document.getElementById("selectedGlassesName");
const selectedGlassesImage = document.getElementById("selectedGlassesImage");
const selectedGlassesDescription = document.getElementById("selectedGlassesDescription");

// ------------------------------------------------------------
// MediaPipe and camera state
// ------------------------------------------------------------
let faceLandmarker = null;
let cameraStream = null;
let animationFrameId = null;
let lastVideoTime = -1;
let isCameraStarting = false;
let lastDetectedShape = null;
let hasValidAnalysis = false;

let faceShapesFromAPI = [];
let framesFromAPI = [];

// The live video is analyzed locally in the browser.
// This code does not capture, save, or upload face photos.

// ------------------------------------------------------------
// Glasses data
// ------------------------------------------------------------
const glasses = {
    "cat-eye": {
        frameId: 3,
        name: "Cat Eye",
        image: "../الصور/cat-eye1.png",
        description: "A lifted shape that highlights the cheekbones."
    },
    "square": {
        frameId: 1,
        name: "Square",
        image: "../الصور/square1.png",
        description: "A structured frame with clean, balanced lines."
    },
    "round": {
        frameId: 2,
        name: "Round",
        image: "../الصور/round1.png",
        description: "A soft rounded frame for a gentle contrast."
    },
    "oval": {
        frameId: 4,
        name: "Oval",
        image: "../الصور/oval1.png",
        description: "A balanced frame with smooth, curved edges."
    }
};

for (const glass of Object.values(glasses)) {
    glass.element = new Image();
    glass.element.src = glass.image;

    glass.element.onerror = () => {
        console.error("Unable to load glasses image:", glass.image);
    };
}

let selectedGlasses = glasses["cat-eye"];

// ------------------------------------------------------------
// Face shape recommendations
// ------------------------------------------------------------
const recommendations = {
    Oval: {
        best: "cat-eye",
        description: "Your face has balanced proportions with gently rounded contours. Geometric, rectangular, and classic frames can complement your features."
    },
    Round: {
        best: "square",
        description: "Your face has soft, rounded contours. Angular and lifted frames can add definition and balance."
    },
    Square: {
        best: "round",
        description: "Your face has a defined jawline and balanced width. Rounded and oval frames can soften angular features."
    },
    Heart: {
        best: "oval",
        description: "Your face is wider around the forehead and gently narrows toward the chin. Oval and rounded frames can create a balanced look."
    }
};

// ------------------------------------------------------------
// Load data from backend
// ------------------------------------------------------------
async function loadBackendData() {
    try {
        const [facesResponse, framesResponse] = await Promise.all([
            fetch(`${API_URL}/faceshapes`),
            fetch(`${API_URL}/frames`)
        ]);

        if (!facesResponse.ok || !framesResponse.ok) {
            throw new Error("Unable to load backend data.");
        }

        faceShapesFromAPI = await facesResponse.json();
        framesFromAPI = await framesResponse.json();

        console.log("Face shapes loaded:", faceShapesFromAPI);
        console.log("Glasses frames loaded:", framesFromAPI);

    } catch (error) {
        console.error("Backend connection error:", error);
    }
}

// ------------------------------------------------------------
// Page navigation
// ------------------------------------------------------------
function isCameraPreviewVisible() {
    const previewSection = document.getElementById("cameraPreviewSection");
    return !previewSection.hidden;
}

function showSection(sectionId) {
    if (sectionId !== "cameraPreviewSection") {
        stopCamera();
    }

    sections.forEach(section => {
        section.hidden = section.id !== sectionId;
    });

    navLinks.forEach(link => {
        const isActive = link.dataset.section === sectionId;

        link.classList.toggle("active", isActive);

        if (isActive) {
            link.setAttribute("aria-current", "page");
        } else {
            link.removeAttribute("aria-current");
        }
    });

    window.scrollTo({ top: 0, behavior: "smooth" });
}

navLinks.forEach(link => {
    link.addEventListener("click", () => {
        showSection(link.dataset.section);
    });
});

document.querySelectorAll("[data-section]").forEach(button => {
    if (button.classList.contains("nav-link")) return;

    button.addEventListener("click", () => {
        showSection(button.dataset.section);
    });
});

document.getElementById("startButton").addEventListener("click", () => {
    showSection("scanSection");
});

document.getElementById("captureAnalyzeButton").addEventListener("click", async () => {
    showSection("cameraPreviewSection");
    await startCamera();
});

viewResultsButton.addEventListener("click", () => {
    if (hasValidAnalysis) {
        showSection("resultSection");
    }
});

viewRecommendationsButton.addEventListener("click", () => {
    if (!hasValidAnalysis || !lastDetectedShape) {
        recommendationIntro.textContent =
            "Please complete a valid face analysis before viewing your personalized recommendations.";

        showSection("recommendationsSection");
        return;
    }

    showRecommendations(lastDetectedShape);
    showSection("recommendationsSection");
});

document.getElementById("tryCameraButton").addEventListener("click", async () => {
    showSection("cameraPreviewSection");

    if (!cameraStream) {
        await startCamera();
    }
});

// ------------------------------------------------------------
// Glasses selection
// ------------------------------------------------------------
function selectGlasses(glassId) {
    const chosen = glasses[glassId];

    if (!chosen) return;

    selectedGlasses = chosen;

    selectedGlassesName.textContent = chosen.name;
    selectedGlassesImage.src = chosen.image;
    selectedGlassesImage.alt = `${chosen.name} glasses`;
    selectedGlassesDescription.textContent = chosen.description;

    document.querySelectorAll(".frame-option").forEach(option => {
        const isSelected = option.dataset.glasses === glassId;

        option.classList.toggle("selected", isSelected);
        option.setAttribute("aria-pressed", String(isSelected));
    });
}

document.querySelectorAll(".frame-option").forEach(option => {
    option.addEventListener("click", () => {
        selectGlasses(option.dataset.glasses);
    });
});

document.querySelectorAll(".try-glasses-button").forEach(button => {
    button.addEventListener("click", () => {
        selectGlasses(button.dataset.glasses);
        showSection("tryOnSection");
    });
});

// ------------------------------------------------------------
// Recommendations from backend
// ------------------------------------------------------------
function clearRecommendations() {
    recommendationIntro.textContent =
        "Analyze your face to see which frames are your best match.";

    document.querySelectorAll("[data-glasses-card]").forEach(card => {
        const label = card.querySelector(".recommendation-label");

        label.textContent = "";
        card.classList.remove("is-best");
    });
}

async function showRecommendations(shape) {
    if (!hasValidAnalysis) {
        clearRecommendations();
        return;
    }

    const shapeId = faceShapeIds[shape];

    if (!shapeId) {
        clearRecommendations();
        return;
    }

    const result = recommendations[shape];

    try {
        const response = await fetch(
            `${API_URL}/face-analysis/recommend/${shapeId}`
        );

        if (!response.ok) {
            throw new Error("Unable to load recommendations.");
        }

        const recommendedFrames = await response.json();

        const recommendedIds = recommendedFrames.map(
            frame => frame.frameId
        );

        const bestGlass = glasses[result.best];
        const bestFrameId = bestGlass.frameId;

        recommendationIntro.textContent =
            `Your face shape is ${shape}. ${result.description}`;

        document.querySelectorAll("[data-glasses-card]").forEach(card => {
            const glassId = card.dataset.glassesCard;
            const glass = glasses[glassId];

            if (!glass) return;

            const label = card.querySelector(".recommendation-label");

            const isBest = glassId === result.best;
            const isSuitable = recommendedIds.includes(glass.frameId);

            label.textContent = isBest
                ? "★ Best Match"
                : isSuitable
                    ? "Suitable for you"
                    : "Explore this style";

            card.classList.toggle("is-best", isBest);
        });

    } catch (error) {
        console.error("Recommendations error:", error);

        recommendationIntro.textContent =
            "Unable to load recommendations. Please make sure the backend is running.";

        document.querySelectorAll("[data-glasses-card]").forEach(card => {
            const label = card.querySelector(".recommendation-label");
            label.textContent = "";
            card.classList.remove("is-best");
        });
    }
}

function updateShapeResult(shape) {
    const result = recommendations[shape];

    if (!result) return;

    hasValidAnalysis = true;

    faceShapeElement.textContent = shape;
    faceShapeDescription.textContent = result.description;

    viewResultsButton.hidden = false;

    if (shape !== lastDetectedShape) {
        lastDetectedShape = shape;
        showRecommendations(shape);
    }
}

// ------------------------------------------------------------
// Face landmark validation
// ------------------------------------------------------------
function isValidLandmark(point) {
    if (!point) return false;

    return (
        Number.isFinite(point.x) &&
        Number.isFinite(point.y) &&
        point.x >= -0.1 &&
        point.x <= 1.1 &&
        point.y >= -0.1 &&
        point.y <= 1.1
    );
}

function hasRequiredLandmarks(landmarks) {
    if (!Array.isArray(landmarks) || landmarks.length <= 454) {
        return false;
    }

    const requiredIndexes = [
        10, 152,
        127, 356,
        234, 454,
        172, 397,
        33, 263
    ];

    return requiredIndexes.every(index =>
        isValidLandmark(landmarks[index])
    );
}

// ------------------------------------------------------------
// Face measurement calculations
// ------------------------------------------------------------
function calculateDistance(point1, point2) {
    if (!isValidLandmark(point1) || !isValidLandmark(point2)) {
        return NaN;
    }

    const x1 = point1.x * camera.videoWidth;
    const y1 = point1.y * camera.videoHeight;
    const x2 = point2.x * camera.videoWidth;
    const y2 = point2.y * camera.videoHeight;

    return Math.hypot(x2 - x1, y2 - y1);
}

function determineFaceShape(faceLength, forehead, cheeks, jaw) {
    const values = [faceLength, forehead, cheeks, jaw];

    if (!values.every(value => Number.isFinite(value) && value > 0)) {
        return null;
    }

    const lengthRatio = faceLength / cheeks;
    const foreheadRatio = forehead / cheeks;
    const jawRatio = jaw / cheeks;

    if (
        !Number.isFinite(lengthRatio) ||
        !Number.isFinite(foreheadRatio) ||
        !Number.isFinite(jawRatio)
    ) {
        return null;
    }

    if (
        lengthRatio < 0.5 || lengthRatio > 3 ||
        foreheadRatio < 0.2 || foreheadRatio > 2.5 ||
        jawRatio < 0.2 || jawRatio > 2.5
    ) {
        return null;
    }

    if (foreheadRatio > 1.05 && jawRatio < 0.85) {
        return "Heart";
    }

    if (lengthRatio < 1.25 && jawRatio >= 0.85 && foreheadRatio >= 0.90) {
        return "Square";
    }

    if (lengthRatio < 1.25) {
        return "Round";
    }

    return "Oval";
}

function calculateFaceMeasurements(landmarks) {
    if (!hasRequiredLandmarks(landmarks)) {
        return false;
    }

    if (!camera.videoWidth || !camera.videoHeight) {
        return false;
    }

    const faceLength = calculateDistance(landmarks[10], landmarks[152]);
    const foreheadWidth = calculateDistance(landmarks[127], landmarks[356]);
    const cheekboneWidth = calculateDistance(landmarks[234], landmarks[454]);
    const jawWidth = calculateDistance(landmarks[172], landmarks[397]);

    const shape = determineFaceShape(
        faceLength,
        foreheadWidth,
        cheekboneWidth,
        jawWidth
    );

    if (!shape) {
        return false;
    }

    faceLengthElement.textContent = `${faceLength.toFixed(1)} px`;
    foreheadWidthElement.textContent = `${foreheadWidth.toFixed(1)} px`;
    cheekboneWidthElement.textContent = `${cheekboneWidth.toFixed(1)} px`;
    jawWidthElement.textContent = `${jawWidth.toFixed(1)} px`;

    updateShapeResult(shape);

    return true;
}

function resetFaceResults() {
    hasValidAnalysis = false;
    lastDetectedShape = null;

    faceLengthElement.textContent = "--";
    foreheadWidthElement.textContent = "--";
    cheekboneWidthElement.textContent = "--";
    jawWidthElement.textContent = "--";

    faceShapeElement.textContent = "No Face Detected";

    faceShapeDescription.textContent =
        "We cannot detect a face right now. Please look straight at the camera.";

    viewResultsButton.hidden = true;

    clearRecommendations();

    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// ------------------------------------------------------------
// Draw the selected glasses on the live camera preview
// ------------------------------------------------------------
function drawGlasses(landmarks) {
    if (!selectedGlasses || !hasRequiredLandmarks(landmarks)) {
        return;
    }

    const image = selectedGlasses.element;

    if (!image.complete || image.naturalWidth === 0) {
        return;
    }

    const leftEye = landmarks[33];
    const rightEye = landmarks[263];

    const leftX = leftEye.x * canvas.width;
    const leftY = leftEye.y * canvas.height;

    const rightX = rightEye.x * canvas.width;
    const rightY = rightEye.y * canvas.height;

    const eyeDistance = Math.hypot(
        rightX - leftX,
        rightY - leftY
    );

    if (!Number.isFinite(eyeDistance) || eyeDistance <= 0) {
        return;
    }

    const centerX = (leftX + rightX) / 2;
    let centerY = (leftY + rightY) / 2 + eyeDistance * 0.05;
    let glassesWidth = eyeDistance * 1.9;

    if (selectedGlasses.name === "Oval") {
        centerY -= eyeDistance * 0.04;
    }

    if (selectedGlasses.name === "Square") {
        centerY -= eyeDistance * 0.08;
        glassesWidth *= 0.90;
    }

    const glassesHeight =
        glassesWidth * (image.naturalHeight / image.naturalWidth);

    const angle = Math.atan2(
        rightY - leftY,
        rightX - leftX
    );

    ctx.save();
    ctx.translate(centerX, centerY);
    ctx.rotate(angle);

    ctx.drawImage(
        image,
        -glassesWidth / 2,
        -glassesHeight / 2,
        glassesWidth,
        glassesHeight
    );

    ctx.restore();
}

// ------------------------------------------------------------
// Camera status and lifecycle
// ------------------------------------------------------------
function updateCameraStatus(message, statusType = "") {
    cameraStatus.textContent = message;
    cameraStatus.className = "camera-status";

    if (statusType) {
        cameraStatus.classList.add(statusType);
    }
}

function stopCamera() {
    if (animationFrameId !== null) {
        cancelAnimationFrame(animationFrameId);
        animationFrameId = null;
    }

    if (cameraStream) {
        cameraStream.getTracks().forEach(track => {
            track.stop();
        });

        cameraStream = null;
    }

    camera.srcObject = null;
    cameraPlaceholder.hidden = false;

    cameraButton.disabled = false;
    cameraButton.textContent = "Open Camera";

    stopCameraButton.hidden = true;

    updateCameraStatus("Camera is off.");

    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

stopCameraButton.addEventListener("click", stopCamera);

cameraButton.addEventListener("click", startCamera);

window.addEventListener("pagehide", stopCamera);

async function startCamera() {
    if (isCameraStarting || cameraStream) {
        return;
    }

    isCameraStarting = true;

    cameraButton.disabled = true;
    cameraButton.textContent = "Loading...";

    updateCameraStatus("Please allow camera access when requested.");

    try {
        if (
            !navigator.mediaDevices ||
            !navigator.mediaDevices.getUserMedia
        ) {
            const error = new Error("CAMERA_NOT_SUPPORTED");
            throw error;
        }

        const newStream = await navigator.mediaDevices.getUserMedia({
            video: { facingMode: "user" },
            audio: false
        });

        if (!isCameraPreviewVisible()) {
            newStream.getTracks().forEach(track => track.stop());
            return;
        }

        cameraStream = newStream;
        camera.srcObject = cameraStream;

        await camera.play();

        if (!isCameraPreviewVisible()) {
            stopCamera();
            return;
        }

        cameraPlaceholder.hidden = true;
        updateCameraStatus("Loading face analysis...");

        if (!faceLandmarker) {
            const vision = await FilesetResolver.forVisionTasks(
                "https://cdn.jsdelivr.net/npm/@mediapipe/tasks-vision/wasm"
            );

            faceLandmarker = await FaceLandmarker.createFromOptions(vision, {
                baseOptions: {
                    modelAssetPath: "https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/1/face_landmarker.task"
                },
                runningMode: "VIDEO",
                numFaces: 1
            });
        }

        if (!isCameraPreviewVisible() || !cameraStream) {
            stopCamera();
            return;
        }

        cameraButton.textContent = "Camera is On";
        cameraButton.disabled = true;

        stopCameraButton.hidden = false;

        updateCameraStatus(
            "Camera is on. Position your face in the frame.",
            "success"
        );

        lastVideoTime = -1;
        detectFace();

    } catch (error) {
        console.error("Camera or face analysis error:", error);

        stopCamera();

        cameraButton.disabled = false;
        cameraButton.textContent = "Try Again";

        if (error.message === "CAMERA_NOT_SUPPORTED") {
            updateCameraStatus(
                "Your browser does not support camera access.",
                "error"
            );
        } else if (error.name === "NotAllowedError") {
            updateCameraStatus(
                "Camera permission is required to analyze your face. Please allow camera access in your browser and try again.",
                "error"
            );
        } else if (error.name === "NotFoundError") {
            updateCameraStatus(
                "No camera was found on this device.",
                "error"
            );
        } else if (error.name === "NotReadableError") {
            updateCameraStatus(
                "The camera is being used by another application.",
                "error"
            );
        } else if (error.name === "SecurityError") {
            updateCameraStatus(
                "Camera access is blocked. Please use a secure connection and check your browser permissions.",
                "error"
            );
        } else {
            updateCameraStatus(
                "Unable to start face analysis. Please try again.",
                "error"
            );
        }
    } finally {
        isCameraStarting = false;
    }
}

// ------------------------------------------------------------
// Face detection
// ------------------------------------------------------------
function detectFace() {
    if (!faceLandmarker || !cameraStream) {
        return;
    }

    if (!isCameraPreviewVisible()) {
        stopCamera();
        return;
    }

    if (camera.readyState < 2) {
        animationFrameId = requestAnimationFrame(detectFace);
        return;
    }

    if (camera.currentTime !== lastVideoTime) {
        lastVideoTime = camera.currentTime;

        try {
            const results = faceLandmarker.detectForVideo(
                camera,
                performance.now()
            );

            if (
                canvas.width !== camera.videoWidth ||
                canvas.height !== camera.videoHeight
            ) {
                canvas.width = camera.videoWidth;
                canvas.height = camera.videoHeight;
            }

            ctx.clearRect(0, 0, canvas.width, canvas.height);

            if (
                results.faceLandmarks &&
                results.faceLandmarks.length > 0
            ) {
                const landmarks = results.faceLandmarks[0];

                const isValid = calculateFaceMeasurements(landmarks);

                if (isValid) {
                    drawGlasses(landmarks);

                    updateCameraStatus(
                        "Face detected successfully!",
                        "success"
                    );
                } else {
                    resetFaceResults();

                    updateCameraStatus(
                        "Unable to analyze the face correctly. Please adjust your position and try again.",
                        "error"
                    );
                }
            } else {
                resetFaceResults();

                updateCameraStatus(
                    "Looking for a face. Please look at the camera."
                );
            }

        } catch (error) {
            console.error("Face detection error:", error);

            resetFaceResults();

            updateCameraStatus(
                "Face analysis failed. Please try again.",
                "error"
            );
        }
    }

    animationFrameId = requestAnimationFrame(detectFace);
}

// ------------------------------------------------------------
// Initial setup
// ------------------------------------------------------------
clearRecommendations();
selectGlasses("cat-eye");
loadBackendData();