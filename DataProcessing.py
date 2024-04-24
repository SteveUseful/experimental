import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OneHotEncoder, StandardScaler
import numpy as np

# Load the data
df = pd.read_csv('C:/dev/Output/synthetic_churn_data.csv')

# Clean the data (if necessary)
# df = df.drop_duplicates()
# df = df.dropna()  # This is a simplistic approach, more sophisticated methods might be needed

# Separate features and target variable
X = df.drop('Churn', axis=1)
y = df['Churn'].apply(lambda x: 1 if x == 'Yes' else 0)

# One-hot encode categorical variables
encoder = OneHotEncoder()
X_categorical = X.select_dtypes(include=['object'])
X_encoded = encoder.fit_transform(X_categorical).toarray()  # Convert to dense array if necessary

# Scale numerical features
scaler = StandardScaler()
X_numerical = X.select_dtypes(include=[np.number])
X_scaled = scaler.fit_transform(X_numerical)

# Combine encoded and scaled features
X_prepared = np.hstack([X_scaled, X_encoded])

# Split the dataset into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X_prepared, y, test_size=0.2, random_state=42)
