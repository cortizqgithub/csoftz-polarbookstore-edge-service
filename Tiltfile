# Build
custom_build(
    # Name of the container image
    ref = 'edge-service',
    tag = 'latest',
    # Command to build the container image
    command = 'mvnw clean -DskipTests=true -Ddocker.image.name=edge-service:latest spring-boot:build-image',
    # Files to watch that trigger a new build
    deps = ['pom.xml', 'src']
)

# Deploy
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml', 'k8s/ingress.yml'])

# Manage
k8s_resource('edge-service', port_forwards='9100:9100')
