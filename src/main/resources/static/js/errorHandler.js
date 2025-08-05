/**
 * Global Error Handler Utility
 * Handles error responses from backend APIs consistently across all frontend pages
 */

/**
 * Parse error response from backend
 * @param {Response} response - Fetch response object
 * @returns {Promise<Object>} Parsed error object with code and message
 */
async function parseErrorResponse(response) {
    try {
        const errorData = await response.json();
        return {
            code: errorData.code || 'UNKNOWN_ERROR',
            message: errorData.message || 'An unknown error occurred',
            status: response.status
        };
    } catch (e) {
        // If response is not JSON, return generic error
        return {
            code: 'PARSE_ERROR',
            message: `HTTP ${response.status}: ${response.statusText}`,
            status: response.status
        };
    }
}

/**
 * Display error message in a consistent format
 * @param {string} containerId - ID of the container to show error
 * @param {string} message - Error message to display
 * @param {string} code - Error code (optional)
 */
function displayError(containerId, message, code = null) {
    const container = document.getElementById(containerId);
    if (!container) {
        console.error('Container not found:', containerId);
        return;
    }

    const errorHtml = `
        <div class="error">
            <div class="error-header">
                <span class="error-icon">⚠️</span>
                <span class="error-title">Error</span>
                ${code ? `<span class="error-code">(${code})</span>` : ''}
            </div>
            <div class="error-message">${message}</div>
        </div>
    `;
    
    container.innerHTML = errorHtml;
}

/**
 * Handle fetch errors with proper error response parsing
 * @param {Response} response - Fetch response object
 * @param {string} containerId - ID of the container to show error
 * @param {string} defaultMessage - Default error message if parsing fails
 * @returns {Promise<boolean>} True if error was handled, false if response was successful
 */
async function handleApiError(response, containerId, defaultMessage = 'Request failed') {
    if (response.ok) {
        return false; // No error
    }

    try {
        const errorData = await parseErrorResponse(response);
        displayError(containerId, errorData.message, errorData.code);
        console.error('API Error:', errorData);
        return true;
    } catch (e) {
        displayError(containerId, defaultMessage);
        console.error('Error parsing response:', e);
        return true;
    }
}

/**
 * Enhanced fetch wrapper with error handling
 * @param {string} url - API endpoint
 * @param {Object} options - Fetch options
 * @param {string} containerId - ID of the container to show errors
 * @param {string} defaultErrorMessage - Default error message
 * @returns {Promise<Object>} Parsed response data or throws error
 */
async function fetchWithErrorHandling(url, options = {}, containerId = null, defaultErrorMessage = 'Request failed') {
    try {
        const response = await fetch(url, options);
        
        if (!response.ok) {
            if (containerId) {
                await handleApiError(response, containerId, defaultErrorMessage);
            }
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        return await response.json();
    } catch (error) {
        if (containerId) {
            displayError(containerId, error.message);
        }
        throw error;
    }
}

/**
 * Show success message
 * @param {string} containerId - ID of the container to show success message
 * @param {string} message - Success message
 */
function displaySuccess(containerId, message) {
    const container = document.getElementById(containerId);
    if (!container) {
        console.error('Container not found:', containerId);
        return;
    }

    const successHtml = `
        <div class="success">
            <div class="success-header">
                <span class="success-icon">✅</span>
                <span class="success-title">Success</span>
            </div>
            <div class="success-message">${message}</div>
        </div>
    `;
    
    container.innerHTML = successHtml;
    
    // Auto-hide success message after 3 seconds
    setTimeout(() => {
        if (container.innerHTML.includes('success')) {
            container.innerHTML = '';
        }
    }, 3000);
}

/**
 * Clear any error or success messages
 * @param {string} containerId - ID of the container to clear
 */
function clearMessages(containerId) {
    const container = document.getElementById(containerId);
    if (container) {
        container.innerHTML = '';
    }
}

/**
 * Show loading state
 * @param {string} containerId - ID of the container to show loading
 * @param {string} message - Loading message
 */
function showLoading(containerId, message = 'Loading...') {
    const container = document.getElementById(containerId);
    if (!container) {
        console.error('Container not found:', containerId);
        return;
    }

    const loadingHtml = `
        <div class="loading">
            <div class="loading-spinner"></div>
            <div class="loading-message">${message}</div>
        </div>
    `;
    
    container.innerHTML = loadingHtml;
}

// Export functions for use in other scripts
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        parseErrorResponse,
        displayError,
        handleApiError,
        fetchWithErrorHandling,
        displaySuccess,
        clearMessages,
        showLoading
    };
} 