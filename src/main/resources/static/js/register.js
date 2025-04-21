function togglePassword(inputId, button) {
    const input = document.getElementById(inputId);
    const isVisible = input.type === "text";
    input.type = isVisible ? "password" : "text";
    button.textContent = isVisible ? "👁️" : "🙈";
}
