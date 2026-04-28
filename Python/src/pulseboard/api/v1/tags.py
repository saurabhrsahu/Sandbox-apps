from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from pulseboard.api.deps import get_db
from pulseboard.db.models import Tag
from pulseboard.schemas.tag import TagCreate, TagOut

router = APIRouter(prefix="/tags", tags=["Tags"])


@router.get("", response_model=list[TagOut])
def list_tags(
    db: Session = Depends(get_db),
    skip: int = Query(default=0, ge=0),
    limit: int = Query(default=100, ge=1, le=500),
) -> list[TagOut]:
    stmt = select(Tag).order_by(Tag.name.asc()).offset(skip).limit(limit)
    return list(db.scalars(stmt).all())


@router.post("", response_model=TagOut, status_code=status.HTTP_201_CREATED)
def create_tag(payload: TagCreate, db: Session = Depends(get_db)) -> TagOut:
    name = payload.name.strip().lower()
    tag = Tag(name=name)
    db.add(tag)
    try:
        db.flush()
    except IntegrityError as exc:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Tag with this name already exists",
        ) from exc
    return TagOut.model_validate(tag)
