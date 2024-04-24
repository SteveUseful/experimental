import pandas as pd
import numpy as np
from faker import Faker
import random

fake = Faker()
np.random.seed(42)

def create_data(n_samples=500000):
    # Define attributes
    data = {
        'CustomerID': [fake.uuid4() for _ in range(n_samples)],
        'ProductCustomerFitScore': np.random.uniform(0, 1, n_samples).round(2),
        'OnboardingCompletion': np.random.uniform(0, 1, n_samples).round(2),
        'CustomerServiceRating': np.random.uniform(0, 5, n_samples).round(1),
        'ValueForMoneyRating': np.random.uniform(0, 5, n_samples).round(1),
        'ReportedBugs': np.random.poisson(1, n_samples),
        'PaymentIssuesLastYear': [random.randint(0, 5) for _ in range(n_samples)],
        'FeatureRequests': np.random.poisson(2, n_samples),
        'CustomerFeedback': [fake.sentence() for _ in range(n_samples)],
        'Churned': np.random.choice([0, 1], n_samples, p=[0.85, 0.15])
    }

    # Creating interaction features
    data['InteractionScore'] = np.round(data['ProductCustomerFitScore'] * data['OnboardingCompletion'] * data['CustomerServiceRating'], 2)
    data['RiskFactor'] = np.round(data['ReportedBugs'] * data['PaymentIssuesLastYear'] / (data['CustomerServiceRating'] + 1), 2)
    
    # Segmentation based on activity
    data['CustomerSegment'] = pd.cut(data['InteractionScore'], bins=[0, 0.5, 1.5, 2.5, np.inf], labels=['Low', 'Medium', 'High', 'Very High'])

    # Create DataFrame
    df = pd.DataFrame(data)
    
    # Save to CSV
    output_file_path = 'C:/dev/Output/Churn_Data1.csv'
    df.to_csv(output_file_path, index=False)
    print(f"Synthetic data file saved to {output_file_path}")

if __name__ == "__main__":
    create_data()
