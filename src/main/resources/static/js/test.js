
document.body.onload = async function() {
    const data = await loadTestData();
    const dataStr = JSON.stringify(data);
    document.body.append(dataStr);

}

const loadTestData = async () => {
    const response = await fetch('/test.json');
    const data = await response.json();
    console.log(data);
    return data;
}