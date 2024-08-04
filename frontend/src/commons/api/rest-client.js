function performRequest(request, callback) {
  fetch(request)
    .then(async (response) => {
      if (response.ok) {
        try {
          const json = await response.json();
          callback(json, response.status, null);
        } catch (error) {
          callback(null, response.status, 'Error parsing JSON');
        }
      } else {
        const errorText = await response.text();
        callback(null, response.status, errorText);
      }
    })
    .catch(function (err) {
      callback(null, 1, err);
    });
}

export { performRequest };

