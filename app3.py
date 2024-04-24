from flask import Flask, request, jsonify, render_template
from flask_cors import CORS
import joblib
import pandas as pd

app = Flask(__name__)
CORS(app)  # Enable CORS if needed

# Load the pipeline, which includes preprocessing and the model
pipeline = joblib.load('best_model2.pkl')

@app.route('/')
def home():
    # Make sure this template name matches your HTML file name
    return render_template('index2.html')

@app.route('/predict', methods=['POST'])
def predict():
    try:
        app.logger.info("Received request for prediction")
        data = request.get_json()
        features = pd.DataFrame([data])
        
        # Validate that all required columns are present
        expected_columns = ['CustomerAge', 'CustomerIncome', 'CustomerEducationLevel', 
                            'SupportInteraction', 'SatisfactionScore', 'UsageFrequency', 
                            'FeatureUsageScore', 'AccountLifetime', 'ContractRenewal', 
                            'MonthlyCharges', 'TotalCharges']
        missing_columns = [col for col in expected_columns if col not in features.columns]
        if missing_columns:
            app.logger.warning(f"Missing columns in input data: {missing_columns}")
            return jsonify({'error': f'Missing columns: {missing_columns}'}), 400
        
        # Predict the probability and classify churn
        probability = pipeline.predict_proba(features)[:, 1][0]
        churn = 'Yes' if probability > 0.5 else 'No'

        # Construct a response to send back to the frontend
        response = {
            'probability': probability,
            'churn': churn
        }

        return jsonify(response)
    except Exception as e:
        # Log the error for debugging purposes
        app.logger.error(f"Prediction error: {e}")
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
