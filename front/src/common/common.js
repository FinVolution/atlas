function handResponse(res) {
    if (res.data.code !== 0) {
        let errMessage = "error code:" + res.data.code + ", error message :" + res.data.message;
        return Promise.reject({
            code: res.data.code,
            message: errMessage
        });
    } else {
        return Promise.resolve(res);
    }
}

export {handResponse};