exports.asyncWrapper = (asyncHandler) => {
  return async (req, res, next) => {
    try {
      return await asyncHandler(req, res, next);
    } catch (err) {
      return next(err);
    }
  };
};
