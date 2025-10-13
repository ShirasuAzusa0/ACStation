import { defineStore } from "pinia";
import { ref } from "vue";

export const useAlertStore = defineStore("alert", () => {
    // 当前提示信息
    const alertMessage = ref("");
    const alertType = ref("info"); // "success" | "warning" | "error" | "info"
    const showAlert = ref(false);

    // 显示提示框设置
    function showAlertMessage(type, msg) {
        alertType.value = type;
        alertMessage.value = msg;
        showAlert.value = true;
        setTimeout(() => (showAlert.value = false), 2500); // 自动2.5秒关闭
    }

    // 手动关闭提示框
    function hideAlert() {
        showAlert.value = false
    }

    return {
        alertMessage,
        alertType,
        showAlert,
        showAlertMessage,
        hideAlert
    }
})