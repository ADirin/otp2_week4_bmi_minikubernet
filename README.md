# Step-by-step: Starting Minikube the right way

**Open your project in IntelliJ and add the YAML files**
1. Open IntelliJ and open your project folder.
2. In the Project tool window (left side) right-click the folder where you want manifests (for example the project root) → New → File.
3. Create file name: deployment.yaml. Click OK.
4. Repeat: New → File → service.yaml.
5.(Optional) You may put both resources in one file named gui-app.yaml separated with ---. The instructions below assume two files.

**Paste these exact contents (copy & replace as needed)**
deployment.yaml
````yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gui-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: gui-app
  template:
    metadata:
      labels:
        app: gui-app
    spec:
      containers:
        - name: gui-app
          # REPLACE the image below with your Docker Hub image if different
          image: amirdirin/sep2_week3_2025_bmidemo
          ports:
            - containerPort: 8080
          env:
            # If you will run the DB inside Minikube, set DB_HOST to the db service name (e.g. "mydb")
            # If DB is on the host, you'll need a reachable host IP (not host.docker.internal) or run the DB in the cluster.
            - name: DB_HOST
              value: "mydb"
            # add other env vars (DB_USER, DB_PASS, etc.) if needed


````
service.yaml
````yaml
apiVersion: v1
kind: Service
metadata:
  name: gui-app-service
spec:
  type: NodePort
  selector:
    app: gui-app
  ports:
    - protocol: TCP
      port: 8080         # service port inside cluster
      targetPort: 8080   # container port
      nodePort: 30080    # node port exposed by Minikube (choose 30000-32767)


````
Important: If your app expects to connect to a DB on your Windows host, host.docker.internal often does NOT work from Minikube. Best practice for students: run the DB in Minikube as well (see optional DB YAML below).

**(Optional) Simple MySQL DB inside Minikube — paste as db.yaml**
If you want the full example with DB inside Minikube, create db.yaml and paste:
db.yaml
````yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mydb
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mydb
  template:
    metadata:
      labels:
        app: mydb
    spec:
      containers:
        - name: mysql
          image: mysql:8
          env:
            - name: MYSQL_ROOT_PASSWORD
              value: rootpassword
          ports:
            - containerPort: 3306
---
apiVersion: v1
kind: Service
metadata:
  name: mydb
spec:
  selector:
    app: mydb
  ports:
    - port: 3306
      targetPort: 3306



````
If you deploy this, set DB_HOST in deployment.yaml to "mydb" and add appropriate DB credentials.


1 **Open PowerShell**
    - Press Start → type “PowerShell” → Run as Administrator 
    - Check that Minikube is installed:
````powershell
minikube version
````
You should see something like minikube version: v1.xx.x

2. **Start your Minikube cluster**
   Type this command exactly (spelling and spaces matter!):

````powershell
minikube start --driver=docker
````
    - minikube → the command name (make sure it’s spelled with an “i”).

    - --driver=docker → tells Minikube to use Docker as its virtualization backend (best for Windows).

3. **Wait for setup to complete**
   You’ll see messages like:
````pgsql
😄  minikube v1.xx.x on Microsoft Windows
✨  Using the docker driver based on user configuration
🏄  Done! kubectl is now configured to use "minikube" cluster
````
That means your Kubernetes cluster is ready.

4. **Check the cluster status**
````powershell
minikube status
````
Expected output:
````makefile
minikube
type: Control Plane
host: Running
kubelet: Running
apiserver: Running
kubeconfig: Configured
````
If apiserver says Stopped, you can fix it by restarting:
````powershell
minikube stop
minikube start --driver=docker
````
5.  **Deploy your application**
    If your YAML file is called gui-app.yaml and saved in C:\Users\amirdi\minikube-app, run:

````powershell
cd "C:\Users\amirdi\minikube-app"
kubectl apply -f gui-app.yaml

````
You should see:
````bash
deployment.apps/gui-app created
service/gui-app-service created
````

6. **Check that your pod is running**
  ````powershell
kubectl get pods
````
look for:
````sql
gui-app-xxxxx   1/1   Running
````
If it’s not running (e.g., says CrashLoopBackOff), check logs:
````powershell
kubectl logs deployment/gui-app

````
7. **Access your app from a browser**
   Start the service tunnel:
````powershell
minikube service gui-app-service --url
````
You’ll see output like:
````powershell
http://127.0.0.1:52865
````
Open that URL in your browser.
Keep the terminal open — closing it stops the tunnel!


8. **Stop or delete the culster (when finished)**
   To stop Minikube but keep everything:
````powershell
minikube stop
````
To delete the cluster completely:
````powershell
minikube delete
````
