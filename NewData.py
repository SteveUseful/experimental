import pandas as pd
import numpy as np

# Define the size of the dataset
num_records = 100000

# Generate synthetic data
data = {
    'Gender': np.random.choice(['Female', 'Male', 'Non-Binary'], num_records),
    'AgeGroup': np.random.choice(['Youth', 'Adult', 'Senior'], num_records),
    'Partner': np.random.choice(['Yes', 'No'], num_records),
    'Dependents': np.random.choice(['Yes', 'No'], num_records),
    'Tenure': np.random.randint(1, 72, size=num_records),
    'ServiceType': np.random.choice(['Basic', 'Standard', 'Premium'], num_records),
    'MultipleServices': np.random.choice(['Yes', 'No'], num_records),
    'ServiceAddons': np.random.choice(['Security', 'Backup', 'Support', 'Media', 'None'], num_records, p=[0.2, 0.2, 0.2, 0.2, 0.2]),
    'Contract': np.random.choice(['Month-to-month', 'One year', 'Two year'], num_records),
    'PaperlessBilling': np.random.choice(['Yes', 'No'], num_records),
    'PaymentMethod': np.random.choice(['Electronic check', 'Mailed check', 'Bank transfer', 'Credit card'], num_records),
    'MonthlyCharges': np.random.uniform(10, 150, num_records).round(2),
    'TotalCharges': np.random.uniform(100, 8000, num_records).round(2),  # Simulated based on tenure and monthly charges
    'Churn': np.random.choice(['Yes', 'No'], num_records)
}

# Create a DataFrame
df = pd.DataFrame(data)

# Save the DataFrame to a CSV file
df.to_csv('C:/dev/Output/Churn_Data1.csv', index=False)
