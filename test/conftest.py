import pytest

def pytest_addoption(parser):
    parser.addoption("--address", action="store")
    parser.addoption("--timeout", action="store")

@pytest.fixture(scope='session')
def address(request):
    craftana_value = request.config.option.address
    if craftana_value is None:
        pytest.skip()
    return craftana_value

@pytest.fixture(scope='session')
def timeout(request):
    timeout_value = request.config.option.timeout
    if timeout_value is None:
        pytest.skip()
    return int(timeout_value)