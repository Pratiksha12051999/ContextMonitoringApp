# Context-Sensing Application

### 1. Imagine you are new to the programming world and not proficient enough in coding. But, you have a brilliant idea where you want to develop a context-sensing application like Project 1.  You come across the Heath-Dev paper and want it to build your application. Specify what Specifications you should provide to the Health-Dev framework to develop the code ideally. (15 points)

**Target Users:**  
The app targets individuals interested in monitoring their health, especially those with chronic conditions or those seeking to track daily health metrics.

**Platform:**  
Initially, the app will be developed for Android, with potential expansion to iOS.

**Sensor Data Requirements:**  
The app will use the camera, flashlight, accelerometer, and gyroscope to track heart rate, respiratory rate, and user movements.

**Frequency of Data Collection:**  
Data will be collected periodically for passive monitoring and on-demand for active checks like heart rate measurements.

**Symptom Input Mechanism:**  
Users will select symptoms from a dropdown menu, and each symptom will be accompanied by a rating bar (0-5) to indicate severity.

**UI Layout:**  
The app will have a clean and intuitive design with health metrics displayed prominently on the home screen and a vertical list for symptom tracking on the symptoms page.

**Navigation:**  
The app will feature a bottom navigation bar with sections for Home, Symptoms, History, and Settings, allowing easy navigation.

**Design Elements:**  
The app will use calming colors like blues and greens, with clear and easy-to-read fonts to create a professional but approachable interface.

**Data Handling and Storage:**  
Health data will be stored locally on the device, with periodic uploads to a secure server. Data will be organized by user profiles for easy retrieval.

**Data Privacy:**  
The app will prioritize data security, with encryption and user control over data management, including options to delete or export data.

**Notifications:**  
Users will receive reminders to log symptoms or check their health metrics, with configurable notification settings.

**Feature Expansion:**  
Future versions may include AI-driven health predictions, integration with wearable devices, and options for telehealth.

**Device Support:**  
The app will support Android devices running version 7.0 (Nougat) or later, ensuring compatibility with various screen sizes.

**Testing Scenarios:**  
Testing will focus on sensor accuracy, UI functionality, and database performance to ensure a smooth user experience.

---

### 2. In Project 1 you have stored the user’s symptoms data in the local server. Using the bHealthy application suite how can you provide feedback to the user and develop a novel application to improve context sensing and use that to generate the model of the user? (15 points)

**Context Tracking:**  
The bHealthy app suite aims to continuously track and gather vital health data, including heart rate, respiratory rate, and symptoms that users report.

**Real-Time Feedback:**  
By analyzing this information in real-time, the app will offer immediate feedback. It will alert users when there are noticeable changes in heart rate, breathing patterns, or symptom trends, providing tailored advice or recommendations.

**Personalization:**  
To personalize these insights, the app first sets a baseline for each user’s usual heart rate, respiratory rate, and symptom patterns. If the app notices significant shifts from this baseline, it will notify the user of potential health concerns.

**Machine Learning Integration:**  
Machine learning could also be integrated into the app to forecast health outcomes or recognize early signs of illness. As more data is accumulated, the app's recommendations and predictions will become more precise and customized.

**Health Reports:**  
Additionally, the bHealthy suite will produce detailed health reports, summarizing key metrics over time. These reports will highlight trends and suggest lifestyle changes or health interventions tailored to the individual.

**Notifications:**  
The app will also keep users engaged with timely notifications, prompting them to act if any significant changes in their health are detected, helping them stay proactive about their health management.

---

### 3. A common assumption is mobile computing is mostly about app development. After completing Project 1 and reading both papers, have your views changed? If yes, what do you think mobile computing is about and why? If no, please explain why you still think mobile computing is mostly about app development, providing examples to support your viewpoint  (10 points)

After completing Project 1 and reviewing the papers, my understanding of mobile computing has changed a lot. Initially, I thought it was mainly about building apps, but now I realize it's so much more—it’s about how mobile devices can understand and interact with their surroundings, creating context-aware applications.

In Project 1, for example, we didn’t just develop an app; we created something that could collect and interpret data from sensors like the accelerometer or camera to deliver meaningful insights based on the user’s environment. These types of applications don’t just run tasks in isolation. They use real-time sensor data—things like location, motion, and even environmental conditions—to make decisions and adjust based on what’s happening around the user.

Instead of just performing set functions, the app was able to analyze data in real-time, offering personalized feedback depending on the user’s status. It became clear that mobile computing involves leveraging real-time data analysis and even predictive modeling to create systems that are aware of their context and can respond intelligently.

What struck me is that mobile computing is no longer just about developing functional apps. It’s about creating systems that are dynamic, smart, and can adapt to the user's needs and environment to deliver more personalized and relevant experiences.

[Youtube Video](https://youtu.be/sO_iue9fe4k)
