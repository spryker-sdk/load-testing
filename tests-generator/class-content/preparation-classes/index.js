exports.get = (prepareDataSteps) => {
    const classList = [];
    for (let prepareDataStep of prepareDataSteps) {
        classList.push(prepareDataStep + 'RequestApi')
    }

    return classList;
}
