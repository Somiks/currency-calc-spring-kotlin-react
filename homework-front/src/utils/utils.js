export const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);
test2
    return fetch(options.url, options).then(res => res.json());
};