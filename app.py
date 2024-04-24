from flask import Flask, request, jsonify, render_template
import joblib
import pandas as pd

app = Flask(__name__)

# Load the best model (ensure this is the correct path to your model)
model = joblib.load('best_model2.pkl')

# If you have a preprocessor pipeline saved, you should load it as well
# preprocessor = joblib.load('preprocessor.pkl') 

@app.route('/')
def home():
    return render_template('index2.html')

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Extract features from the request
        data = request.get_json()
        features = pd.DataFrame([data])

        # If you have preprocessing steps, apply them to the incoming features
        # features = preprocessor.transform(features)

        # Predict the probability and classify churn
        probability = model.predict_proba(features)[:, 1][0]
        churn = 'Yes' if probability > 0.5 else 'No'

        # Construct a response object to send back to the frontend
        response = {
            'probability': probability,
            'churn': churn
        }

        # Return the response as JSON
        return jsonify(response)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
