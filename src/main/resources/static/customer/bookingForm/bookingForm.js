document.addEventListener("DOMContentLoaded", function () {
    const checkInDate = document.getElementById("checkInDate");
    const checkOutDate = document.getElementById("checkOutDate");
    const checkInHour = document.getElementById("checkInHour");
    const checkOutHour = document.getElementById("checkOutHour");
    const roomQuantity = document.getElementById("roomQuantitySelect");
    const priceDisplay = document.getElementById("priceDisplay");
<<<<<<< HEAD
    const maxOccupancy = parseInt(document.getElementById("maxPeople").innerText) || 1;

    const adultInput = document.getElementById("adultInput");
    const elderInput = document.getElementById("elderInput");
    const childInput = document.getElementById("childInput");
    const underThreeInput = document.getElementById("underThreeInput");
    const teenInput = document.getElementById("teenInput");
    const errorDiv = document.getElementById("peopleError");

    function populateSelect(select, min, max, selectedValue) {
        select.innerHTML = "";
        for (let i = min; i <= max; i++) {
            const option = document.createElement("option");
            option.value = i;
            option.textContent = i;
            select.appendChild(option);
        }
        if (selectedValue <= max) {
            select.value = selectedValue;
        } else {
            select.value = max;
        }
    }

    function updatePeopleSelects() {
        const quantity = parseInt(roomQuantity.value) || 1;
        const minAdult = quantity;
        const totalMax = quantity * maxOccupancy;

        const currentAdult = parseInt(adultInput.value);
        const defaultAdult = isNaN(currentAdult) || currentAdult < minAdult || currentAdult > totalMax
            ? minAdult
            : currentAdult;

        populateSelect(adultInput, minAdult, totalMax, defaultAdult);
        adultInput.value = defaultAdult;

        const fields = [
            { id: "elderInput", value: parseInt(elderInput.value) || 0 },
            { id: "childInput", value: parseInt(childInput.value) || 0 },
            { id: "underThreeInput", value: parseInt(underThreeInput.value) || 0 },
            { id: "teenInput", value: parseInt(teenInput.value) || 0 },
        ];

        for (const field of fields) {
            const othersTotal = fields.reduce((sum, f) => f.id !== field.id ? sum + f.value : sum, 0) + defaultAdult;
            const remaining = Math.max(totalMax - othersTotal, 0);
            populateSelect(document.getElementById(field.id), 0, remaining, field.value);
        }
    }

=======
    const pricePerRoom = parseFloat(document.getElementById("priceValue").dataset.price) || 0;
    const peopleSelect = document.getElementById("totalPeopleSelect");
    const maxOccupancy = parseInt(document.getElementById("maxPeople").innerText) || 1;

>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
    function calculatePrice() {
        const inDate = checkInDate.value;
        const outDate = checkOutDate.value;
        const inHour = parseInt(checkInHour.value);
        const outHour = parseInt(checkOutHour.value);
        const quantity = parseInt(roomQuantity.value);

<<<<<<< HEAD
        if (!inDate || !outDate || isNaN(inHour) || isNaN(outHour) || isNaN(quantity)) return;

        const start = new Date(inDate);
        start.setHours(inHour);
        const end = new Date(outDate);
        end.setHours(outHour);
        const diffMs = end - start;
        const diffHours = diffMs / (1000 * 60 * 60);
        const slots = Math.ceil(diffHours / 12);

        if (diffHours <= 0) {
            priceDisplay.innerText = "Tổng giá: 0 VND (0 giờ, 0 phòng)";
            document.getElementById("hiddenTotalPrice").value = 0;
            return;
        }

        const adultVal = parseInt(adultInput.value) || 0;
        const elderVal = parseInt(elderInput.value) || 0;
        const childVal = parseInt(childInput.value) || 0;
        const underThreeVal = parseInt(underThreeInput.value) || 0;
        const teenVal = parseInt(teenInput.value) || 0;

        const priceAdult = parseFloat(document.getElementById("priceAdult").dataset.price) || 0;
        const priceElder = parseFloat(document.getElementById("priceElder").dataset.price) || 0;
        const priceChild = parseFloat(document.getElementById("priceChild").dataset.price) || 0;
        const priceUnderThree = parseFloat(document.getElementById("priceUnderThree").dataset.price) || 0;
        const priceTeen = parseFloat(document.getElementById("priceTeen").dataset.price) || 0;

        const totalPerSlot =
            (adultVal * priceAdult) +
            (elderVal * priceElder) +
            (childVal * priceChild) +
            (underThreeVal * priceUnderThree) +
            (teenVal * priceTeen);

        const total = totalPerSlot * slots;

        priceDisplay.innerText = `Tổng giá: ${total.toLocaleString()} VND (${Math.floor(diffHours)} giờ, ${quantity} phòng)`;
        document.getElementById("hiddenTotalPrice").value = total;
    }

    function validatePeople() {
        const quantity = parseInt(roomQuantity.value) || 1;
        const minAdult = quantity;
        const totalMax = quantity * maxOccupancy;

        const adults = parseInt(adultInput.value) || 0;
        const elders = parseInt(elderInput.value) || 0;
        const children = parseInt(childInput.value) || 0;
        const underThree = parseInt(underThreeInput.value) || 0;
        const teens = parseInt(teenInput.value) || 0;

        const totalPeople = adults + elders + children + underThree + teens;

        if (adults < minAdult) {
            errorDiv.innerText = `Cần ít nhất ${minAdult} người lớn cho ${quantity} phòng.`;
            return false;
        } else if (totalPeople > totalMax) {
            errorDiv.innerText = "Tổng số người vượt quá sức chứa tối đa.";
            return false;
        } else {
            errorDiv.innerText = "";
            return true;
        }
    }

    const allInputs = [
        checkInDate, checkOutDate, checkInHour, checkOutHour, roomQuantity,
        adultInput, elderInput, childInput, underThreeInput, teenInput
    ];

    allInputs.forEach(input => {
        input.addEventListener("change", () => {
            updatePeopleSelects();
            validatePeople();
            calculatePrice();
        });
    });

    updatePeopleSelects();
    validatePeople();
    calculatePrice();

    document.getElementById("bookingSubmitForm").addEventListener("submit", function (e) {
        if (!validatePeople()) {
            e.preventDefault();
            return;
        }

        const adultVal = parseInt(adultInput.value) || 0;
        const elderVal = parseInt(elderInput.value) || 0;
        const teenVal = parseInt(teenInput.value) || 0;
        const childVal = parseInt(childInput.value) || 0;
        const underThreeVal = parseInt(underThreeInput.value) || 0;

        const numberOfAdult = adultVal + elderVal;
        const numberOfChild = teenVal + childVal + underThreeVal;
        const totalPeople = numberOfAdult + numberOfChild;

=======
        if (inDate && outDate && !isNaN(inHour) && !isNaN(outHour) && !isNaN(quantity)) {
            const start = new Date(inDate);
            start.setHours(inHour);

            const end = new Date(outDate);
            end.setHours(outHour);

            const diffMs = end - start;
            const diffHours = diffMs / (1000 * 60 * 60);

            if (diffHours <= 0) {
                priceDisplay.innerText = "Tổng giá: 0 VND (0 giờ, 0 phòng)";
                document.getElementById("hiddenTotalPrice").value = 0;
                return;
            }

            const slots = Math.ceil(diffHours / 12);
            const total = pricePerRoom * slots * quantity;

            priceDisplay.innerText = `Tổng giá: ${total.toLocaleString()} VND (${Math.floor(diffHours)} giờ, ${quantity} phòng)`;

            document.getElementById("hiddenTotalPrice").value = total;
        }
    }

    function updatePeopleOptions() {
        const quantity = parseInt(roomQuantity.value);

        if (!isNaN(quantity)) {
            const min = quantity;
            const max = quantity * maxOccupancy;

            peopleSelect.innerHTML = "";
            for (let i = min; i <= max; i++) {
                const option = document.createElement("option");
                option.value = i;
                option.textContent = i;
                peopleSelect.appendChild(option);
            }
            peopleSelect.value = min;
        }
    }

    [checkInDate, checkOutDate, checkInHour, checkOutHour, roomQuantity].forEach(input => {
        input.addEventListener("change", () => {
            calculatePrice();
            updatePeopleOptions();
        });
    });

    updatePeopleOptions();
    calculatePrice();

    document.getElementById("bookingSubmitForm").addEventListener("submit", function () {
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
        document.getElementById("hiddenCheckInDate").value = checkInDate.value;
        document.getElementById("hiddenCheckOutDate").value = checkOutDate.value;
        document.getElementById("hiddenCheckInHour").value = checkInHour.value;
        document.getElementById("hiddenCheckOutHour").value = checkOutHour.value;
        document.getElementById("hiddenRoomQuantity").value = roomQuantity.value;
<<<<<<< HEAD
        document.getElementById("hiddenTotalPeople").value = totalPeople;
        document.getElementById("hiddenNumberOfAdult").value = numberOfAdult;
        document.getElementById("hiddenNumberOfChild").value = numberOfChild;

        document.getElementById("hiddenAdult").value = adultVal;
        document.getElementById("hiddenElder").value = elderVal;
        document.getElementById("hiddenTeen").value = teenVal;
        document.getElementById("hiddenChild").value = childVal;
        document.getElementById("hiddenUnderThree").value = underThreeVal;
    });
});
=======
        document.getElementById("hiddenTotalPeople").value = peopleSelect.value;
        document.getElementById("hiddenPaymentMethod").value = document.getElementById("paymentMethodSelect").value;
    });
});
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
