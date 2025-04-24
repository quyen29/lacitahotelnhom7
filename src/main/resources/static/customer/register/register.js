//document.addEventListener("DOMContentLoaded", function () {
//    updateUserMenu();
//
//    const form = document.querySelector("form");
//    form.addEventListener("submit", function (e) {
//        const fullname = document.querySelector("[name='fullName']").value.trim();
//        const phone = document.querySelector("[name='phoneNumber']").value.trim();
//        const password = document.querySelector("[name='password']").value;
//        const cccd = document.querySelector("[name='ssn']").value.trim();
//        const dob = new Date(document.querySelector("[name='DOB']").value);
//        const email = document.querySelector("[name='email']").value.trim();
//        const gender = document.querySelector("[name='gender']").value;
//        const now = new Date();
//        const minDOB = new Date(now.getFullYear() - 18, now.getMonth(), now.getDate());
//
//        let isValid = true;
//        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
//        const phoneRegex = /^\d{10}$/;
//        const cccdRegex = /^\d{12}$/;
//
//        toggleError("fullnameError", !fullname, () => isValid = false);
//        toggleError("phoneError", !phoneRegex.test(phone), () => isValid = false);
//        toggleError("regPasswordError", password.length < 8, () => isValid = false);
//        toggleError("cccdError", !cccdRegex.test(cccd), () => isValid = false);
//        toggleError("dobError", !dob || dob > minDOB, () => isValid = false);
//        toggleError("emailError", !emailRegex.test(email), () => isValid = false);
//        toggleError("genderError", !gender, () => isValid = false);
//
//        if (!isValid) {
//            e.preventDefault();
//        }
//    });
//
//    function toggleError(id, condition, onError) {
//        const errorDiv = document.getElementById(id);
//        if (condition) {
//            errorDiv.style.display = "block";
//            onError();
//        } else {
//            errorDiv.style.display = "none";
//        }
//    }
//});